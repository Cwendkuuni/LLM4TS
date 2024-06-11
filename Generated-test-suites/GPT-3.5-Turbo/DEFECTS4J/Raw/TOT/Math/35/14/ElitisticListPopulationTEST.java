import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        double elitismRate = 0.5;
        population = new ElitisticListPopulation(chromosomes, 10, elitismRate);
    }

    @Test
    public void testConstructorWithChromosomes() {
        // Check that the population size is set correctly
        assertEquals(10, population.getPopulationLimit());

        // Check that elitism rate is set correctly
        assertEquals(0.5, population.getElitismRate(), 0.0);

        // Check that the chromosomes list is initialized correctly
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        // Create population without chromosomes
        population = new ElitisticListPopulation(10, 0.5);

        // Check that the population size is set correctly
        assertEquals(10, population.getPopulationLimit());

        // Check that elitism rate is set correctly
        assertEquals(0.5, population.getElitismRate(), 0.0);

        // Check that the chromosomes list is initialized correctly
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowRange() {
        population.setElitismRate(-0.5);
        fail("Expected exception OutOfRangeException");
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveRange() {
        population.setElitismRate(1.5);
        fail("Expected exception OutOfRangeException");
    }

    @Test
    public void testSetElitismRateWithinRange() {
        population.setElitismRate(0.3);

        // Check that the elitism rate is set correctly
        assertEquals(0.3, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        // Add some chromosomes to the population
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(0.5);
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.8);
        population.addChromosome(chromosome2);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(0.6);
        population.addChromosome(chromosome3);

        // Create the next generation
        Population nextGeneration = population.nextGeneration();

        // Check that the next generation size is correct
        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());

        // Check that the best chromosomes are copied to the next generation
        assertTrue(nextGeneration.getChromosomes().contains(chromosome2));
        assertTrue(nextGeneration.getChromosomes().contains(chromosome3));
        assertFalse(nextGeneration.getChromosomes().contains(chromosome1));
    }
}