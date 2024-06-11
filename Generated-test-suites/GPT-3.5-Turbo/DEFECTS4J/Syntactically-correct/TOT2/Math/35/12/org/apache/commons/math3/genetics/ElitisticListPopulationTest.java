import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        // Create a list of test chromosomes
        List<Chromosome> testChromosomes = new ArrayList<>();
        // Add some test chromosomes

        ElitisticListPopulation population = new ElitisticListPopulation(testChromosomes, 10, 0.5);
        
        assertNotNull(population);
        // Check if populationLimit is set properly
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        assertNotNull(population);
        // Check if populationLimit is set properly
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testNextGeneration() {
        // Create a population with some chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        // Add some test chromosomes

        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        // Check if size of next generation is correct
        assertEquals(5, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(0.7);
        
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeLow() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeHigh() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(1.1);
    }

}