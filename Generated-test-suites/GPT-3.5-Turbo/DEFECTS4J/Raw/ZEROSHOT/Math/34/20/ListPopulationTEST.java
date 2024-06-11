import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationEmptyConstructor() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertNotNull(population);
        assertEquals(2, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorPopulationLimitExceeded() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(population.getChromosomes(), population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome1.setFitness(10);
        chromosome2.setFitness(20);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(Collections.nCopies(5, new Chromosome()));
        population.setPopulationLimit(4);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        Iterator<Chromosome> iterator = population.iterator();
        assertFalse(iterator.hasNext());
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome, iterator.next());
        assertFalse(iterator.hasNext());
    }

}