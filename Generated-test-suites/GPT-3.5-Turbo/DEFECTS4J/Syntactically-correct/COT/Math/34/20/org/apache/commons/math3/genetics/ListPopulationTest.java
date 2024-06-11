import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    // Test case for ListPopulation(int populationLimit) method
    @Test
    public void testConstructor1() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    // Test case for ListPopulation(List<Chromosome> chromosomes, int populationLimit) method
    @Test
    public void testConstructor2() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 100);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for setChromosomes(List<Chromosome> chromosomes) method
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(100);
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for addChromosomes(Collection<Chromosome> chromosomeColl) method
    @Test
    public void testAddChromosomes() {
        Collection<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(100);
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes.size(), population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    // Test case for getChromosomes() method
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertNotSame(chromosomes, population.getChromosomes());
    }

    // Test case for getChromosomeList() method
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomeList = population.getChromosomeList();
        assertNotSame(chromosomeList, population.getChromosomeList());
    }

    // Test case for addChromosome(Chromosome chromosome) method
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        ListPopulation population = new ListPopulation(100);
        population.addChromosome(chromosome);
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    // Test case for getFittestChromosome() method
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(0));
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(10));
        ListPopulation population = new ListPopulation(chromosomes, 100);
        assertEquals(10, population.getFittestChromosome().getFitness());
    }

    // Test case for getPopulationLimit() method
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(100, population.getPopulationLimit());
    }

    // Test case for setPopulationLimit(int populationLimit) method
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        population.setPopulationLimit(200);
        assertEquals(200, population.getPopulationLimit());
    }

    // Test case for getPopulationSize() method
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(0, population.getPopulationSize());
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(0));
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(10));
        ListPopulation population = new ListPopulation(chromosomes, 100);
        Iterator<Chromosome> iterator = population.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(count, chromosomes.size());
    }
}