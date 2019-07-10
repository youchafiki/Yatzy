package yatzy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Yatzy {

	public static int chance(int d1, int d2, int d3, int d4, int d5) {
		return IntStream.of(d1, d2, d3, d4, d5).sum();
	}

	public static int yatzy(int... dice) {
		return IntStream.of(dice).boxed().allMatch(Integer.valueOf(dice[0])::equals) ? 50 : 0;
	}

	public static int ones(int d1, int d2, int d3, int d4, int d5) {
		return occurrenceOfFiltredElements(d1, d2, d3, d4, d5, 1);
	}

	public static int twos(int d1, int d2, int d3, int d4, int d5) {
		return occurrenceOfFiltredElements(d1, d2, d3, d4, d5, 2);
	}

	public static int threes(int d1, int d2, int d3, int d4, int d5) {
		return occurrenceOfFiltredElements(d1, d2, d3, d4, d5, 3);
	}

	private static int occurrenceOfFiltredElements(int d1, int d2, int d3, int d4, int d5, int element) {
		return IntStream.of(d1, d2, d3, d4, d5).filter(dice -> dice == element).sum();
	}

	private int occurrenceOfFiltredElements(int element) {
		return IntStream.of(dice).filter(d -> d == element).sum();
	}

	protected int[] dice;

	public Yatzy(int d1, int d2, int d3, int d4, int _5) {
		dice = IntStream.of(d1, d2, d3, d4, _5).toArray();
	}

	public int fours() {
		return occurrenceOfFiltredElements(4);

	}

	public int fives() {
		return occurrenceOfFiltredElements(5);
	}

	public int sixes() {
		return occurrenceOfFiltredElements(6);
	}

	public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
		return scoreBySumOfOccurrence(d1, d2, d3, d4, d5, 2);
	}

	private static int scoreBySumOfOccurrence(int d1, int d2, int d3, int d4, int d5, int nb_occurs) {
		if (nb_occurs < 2)
			throw new RuntimeException("Le nombre d'occurence doit être supérieur ou égal à deux");
		int occurs = 0;
		int currentElement = 0;
		List<Integer> dices = Arrays.asList(d1, d2, d3, d4, d5);
		Collections.sort(dices, (i, j) -> j.compareTo(i));
		for (int item : dices) {
			if (currentElement == item) {
				if (++occurs == nb_occurs) {
					break;
				}
			} else {
				occurs = 1;
				currentElement = item;
			}
		}

		if (occurs == nb_occurs)
			return occurs * currentElement;

		return 0;
	}

	public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
		AtomicInteger occurs = new AtomicInteger(0);
		AtomicInteger currentElement = new AtomicInteger(0);
		AtomicInteger nbOfPair = new AtomicInteger(0);
		AtomicInteger sum = new AtomicInteger(0);
		AtomicInteger recordedKindOfPair = new AtomicInteger(0);

		IntStream.of(d1, d2, d3, d4, d5).sorted().forEach(item -> {
			if (currentElement.get() == item) {
				if (occurs.incrementAndGet() == 2 && recordedKindOfPair.get() != item) {
					sum.addAndGet(item * 2);
					nbOfPair.incrementAndGet();
					occurs.set(0);
					recordedKindOfPair.set(item);
					currentElement.set(0);
				}
			} else {
				occurs.set(1);
				currentElement.set(item);
			}
		});
		if (nbOfPair.get() != 2)
			return 0;
		return sum.get();
	}

	public static int four_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
		return scoreBySumOfOccurrence(d1, d2, d3, d4, d5, 4);
	}

	public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
		return scoreBySumOfOccurrence(d1, d2, d3, d4, d5, 3);
	}

	public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {

		List<Integer> dices = Arrays.asList(d1, d2, d3, d4, d5);
		Collections.sort(dices);

		return verifyOrder(dices, 1) ? 15 : 0;
	}

	private static boolean verifyOrder(Collection<Integer> dices, int offset) {

		int index = offset;
		for (int item : dices) {
			if (index != item)
				break;
			else
				index++;
		}
		return (index == offset + 5) ? true : false;
	}

	public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {

		List<Integer> dices = Arrays.asList(d1, d2, d3, d4, d5);
		Collections.sort(dices);

		return verifyOrder(dices, 2) ? 20 : 0;
	}

	public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
		AtomicInteger sum = new AtomicInteger(0);
		Map<Integer, Long> result = Stream.of(d1, d2, d3, d4, d5)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		if (result.values().size() != 2)
			return 0;
		else
			result.keySet().stream().forEach(key -> sum.set(sum.get() + key * result.get(key).intValue()));
		return sum.get();
	}
}
