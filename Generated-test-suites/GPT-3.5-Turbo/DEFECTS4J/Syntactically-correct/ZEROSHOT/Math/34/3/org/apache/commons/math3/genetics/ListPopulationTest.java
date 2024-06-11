import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

	private ListPopulation listPopulation;
	private int populationLimit;

	@Before
	public void setUp() {
		populationLimit = 10;
		listPopulation = new ListPopulation(populationLimit) {
			public boolean isFinished() {
				return false;
			}
		};
	}

	@Test
	public void testSetChromosomes() {
		listPopulation.setChromosomes(null);
		assertEquals(0, listPopulation.getChromosomes().size());

		List<Chromosome> chromosomes = new ArrayList<>();
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		chromosomes.add(chromosome1);
		chromosomes.add(chromosome2);

		listPopulation.setChromosomes(chromosomes);
		assertEquals(chromosomes, listPopulation.getChromosomes());
	}

	@Test(expected = NumberIsTooLargeException.class)
	public void testSetChromosomesWithTooLargeList() {
		List<Chromosome> chromosomes = new ArrayList<>();
		for (int i = 0; i < populationLimit + 1; i++) {
			chromosomes.add(new Chromosome());
		}

		listPopulation.setChromosomes(chromosomes);
	}

	@Test
	public void testAddChromosomes() {
		listPopulation.addChromosomes(null);
		assertEquals(0, listPopulation.getChromosomes().size());

		List<Chromosome> chromosomes = new ArrayList<>();
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		chromosomes.add(chromosome1);
		chromosomes.add(chromosome2);

		listPopulation.addChromosomes(chromosomes);
		assertEquals(chromosomes, listPopulation.getChromosomes());
	}

	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosomesWithTooLargeList() {
		List<Chromosome> chromosomes = new ArrayList<>();
		for (int i = 0; i < populationLimit + 1; i++) {
			chromosomes.add(new Chromosome());
		}

		listPopulation.addChromosomes(chromosomes);
	}

	@Test
	public void testGetFittestChromosome() {
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		chromosome1.setFitness(10);
		chromosome2.setFitness(20);

		listPopulation.addChromosome(chromosome1);
		listPopulation.addChromosome(chromosome2);

		assertEquals(chromosome2, listPopulation.getFittestChromosome());
	}

	@Test
	public void testGetPopulationLimit() {
		assertEquals(populationLimit, listPopulation.getPopulationLimit());
	}

	@Test(expected = NotPositiveException.class)
	public void testSetPopulationLimitWithNegativeNumber() {
		listPopulation.setPopulationLimit(-10);
	}

	@Test(expected = NotPositiveException.class)
	public void testSetPopulationLimitWithZero() {
		listPopulation.setPopulationLimit(0);
	}

	@Test(expected = NumberIsTooSmallException.class)
	public void testSetPopulationLimitWithSmallerValue() {
		listPopulation.setPopulationLimit(listPopulation.getPopulationSize() - 1);
	}

	@Test
	public void testSetPopulationLimitWithValidValue() {
		listPopulation.setPopulationLimit(listPopulation.getPopulationSize());
		assertEquals(listPopulation.getPopulationSize(), listPopulation.getPopulationLimit());
	}

	@Test
	public void testGetPopulationSize() {
		assertEquals(0, listPopulation.getPopulationSize());

		Chromosome chromosome = new Chromosome();
		listPopulation.addChromosome(chromosome);

		assertEquals(1, listPopulation.getPopulationSize());
	}

	@Test
	public void testIterator() {
		for (int i = 0; i < populationLimit; i++) {
			listPopulation.addChromosome(new Chromosome());
		}

		Iterator<Chromosome> iterator = listPopulation.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}

		assertEquals(count, listPopulation.getPopulationSize());
	}

	private class Chromosome implements Comparable<Chromosome> {
		private double fitness;

		public void setFitness(double fitness) {
			this.fitness = fitness;
		}

		@Override
		public int compareTo(Chromosome other) {
			return Double.compare(this.fitness, other.fitness);
		}
	}
}