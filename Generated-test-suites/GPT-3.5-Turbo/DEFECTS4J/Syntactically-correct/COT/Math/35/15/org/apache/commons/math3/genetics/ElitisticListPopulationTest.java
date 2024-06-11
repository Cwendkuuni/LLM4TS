import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    // Test constructor with specified chromosomes, population limit, and elitism rate
    @Test
    public void testConstructorWithParameters() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // Create a new instance of ElitisticListPopulation with specified parameters
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Check if the chromosome list is initialized correctly
        assertEquals(chromosomes, population.getChromosomes());

        // Check if the population limit is set correctly
        assertEquals(10, population.getPopulationLimit());

        // Check if the elitism rate is set correctly
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Test constructor with specified population limit and elitism rate
    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        // Create a new instance of ElitisticListPopulation with specified population limit and elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Check if the chromosome list is initialized correctly
        assertNotNull(population.getChromosomes());

        // Check if the population limit is set correctly
        assertEquals(10, population.getPopulationLimit());

        // Check if the elitism rate is set correctly
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Test nextGeneration method
    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));

        // Create a new instance of ElitisticListPopulation with specified parameters
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.5);

        // Create the next generation
        Population nextGeneration = population.nextGeneration();

        // Check if the next generation has the correct number of chromosomes
        assertEquals(3, nextGeneration.getChromosomes().size());

        // Check if the best chromosomes are copied to the next generation
        assertTrue(nextGeneration.getChromosomes().containsAll(chromosomes.subList(3, 5)));
    }

    // Test setElitismRate method with valid rate
    @Test
    public void testSetElitismRateWithValidRate() {
        // Create a new instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        // Set the elitism rate to a valid value
        population.setElitismRate(0.9);

        // Check if the elitism rate is set correctly
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Test setElitismRate method with rate outside the [0, 1] range
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidRate() {
        // Create a new instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        // Set the elitism rate to an invalid value
        population.setElitismRate(-0.5);
    }

}