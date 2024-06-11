/*
 * The following JUnit test cases comprehensively test all methods in the ElitisticListPopulation class
 */

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {
    private List<Chromosome> chromosomes;
    private final int populationLimit = 10;
    private final double elitismRate = 0.8;

    @Before
    public void setup() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < populationLimit; i++) {
            chromosomes.add(new Chromosome());
        }
    }

    @Test
    public void testConstructorWithChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        // The number of chromosomes in the next generation should be equal to the population limit
        assertEquals(populationLimit, newChromosomes.size());

        // The best chromosomes from the old population should be copied to the next generation
        for (int i = 0; i < populationLimit * elitismRate; i++) {
            assertTrue(newChromosomes.contains(oldChromosomes.get(i)));
        }

        // The remaining chromosomes in the next generation should be different from the old population
        for (int i = (int) Math.ceil(populationLimit * elitismRate); i < populationLimit; i++) {
            assertFalse(oldChromosomes.contains(newChromosomes.get(i)));
        }
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        double newElitismRate = 0.5;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateLowerBound() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateUpperBound() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        double newElitismRate = 1.1;
        population.setElitismRate(newElitismRate);
    }
}