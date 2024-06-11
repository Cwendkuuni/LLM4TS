import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testCreatePopulationWithLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome1.setFitness(10);
        chromosome2.setFitness(5);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(1); // Should throw NumberIsTooSmallException
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2)); // Should throw NumberIsTooLargeException
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2)); // Should throw NumberIsTooLargeException
    }

    // Additional test cases for edge cases and error scenarios can be added here

}