import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulation() {
        // Test constructor with population limit
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithChromosomes() {
        // Test constructor with chromosomes and population limit
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(2, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        // Test constructor with negative population limit
        ListPopulation population = new ListPopulation(-10);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        // Test constructor with null chromosomes
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithLargeChromosomes() {
        // Test constructor with chromosomes larger than population limit
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        // Test setChromosomes method
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        // Test setChromosomes method with null chromosomes
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithLargeChromosomes() {
        // Test setChromosomes method with chromosomes larger than population limit
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        // Test addChromosomes method
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(new ArrayList<Chromosome>());
        assertEquals(0, population.getPopulationSize());
        
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithLargeChromosomes() {
        // Test addChromosomes method with chromosomes larger than population limit
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        // Test getChromosomes method
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(0, chromosomes.size());
        
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        assertEquals(0, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList() {
        // Test getChromosomeList method
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(0, chromosomes.size());
        
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        assertEquals(2, chromosomes.size());
    }

    @Test
    public void testAddChromosome() {
        // Test addChromosome method
        ListPopulation population = new ListPopulation(2);
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithLargePopulation() {
        // Test addChromosome method with population exceeding the population limit
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        // Test getFittestChromosome method
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        // Test getPopulationLimit method
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        // Test setPopulationLimit method
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        // Test setPopulationLimit method with negative population limit
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallPopulationLimit() {
        // Test setPopulationLimit method with new population limit smaller than current number of chromosomes
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        // Test getPopulationSize method
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        // Test iterator method
        ListPopulation population = new ListPopulation(10);
        assertFalse(population.iterator().hasNext());
        
        population.addChromosome(new Chromosome());
        assertTrue(population.iterator().hasNext());
    }

}