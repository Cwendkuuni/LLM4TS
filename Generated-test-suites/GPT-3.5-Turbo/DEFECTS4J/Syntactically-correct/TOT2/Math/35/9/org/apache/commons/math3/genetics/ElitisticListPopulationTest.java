import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private static final double DELTA = 1e-15;

    @Test
    public void testConstructor1() {
        // Test constructor with list of chromosomes
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Act
        int actualSize = population.getPopulationSize();

        // Assert
        assertEquals(0, actualSize);
    }

    @Test
    public void testConstructor2() {
        // Test constructor without list of chromosomes
        // Arrange & Act
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Assert
        assertNotNull(population);
    }

    @Test
    public void testNextGeneration() {
        // Test nextGeneration method
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Act
        Population nextGen = population.nextGeneration();

        // Assert
        assertEquals(2, nextGen.getPopulationSize());
    }

    @Test
    public void testSetElitismRate() {
        // Test setElitismRate method
        // Arrange
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Act
        population.setElitismRate(0.5);

        // Assert
        assertEquals(0.5, population.getElitismRate(), DELTA);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateException() {
        // Test setElitismRate method with out of range value
        // Arrange
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Act
        population.setElitismRate(1.5);
    }

}