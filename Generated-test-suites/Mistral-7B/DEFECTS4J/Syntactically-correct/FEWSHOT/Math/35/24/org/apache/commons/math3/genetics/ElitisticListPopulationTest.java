package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes and population limit.
     */
    @Test
    public void testConstructorWithInitialChromosomesAndPopulationLimit() {
        List<Chromosome> initialChromosomes = Collections.nCopies(10, new StubChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 15, 0.5);
        assertEquals(15, population.size());
        assertTrue(population instanceof ElitisticListPopulation);
    }

    /**
     * Test constructor with only population limit.
     */
    @Test
    public void testConstructorWithOnlyPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        assertEquals(15, population.size());
        assertTrue(population instanceof ElitisticListPopulation);
    }

    /**
     * Test nextGeneration method with initial chromosomes.
     */
    @Test
    public void testNextGenerationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = Collections.nCopies(10, new StubChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 15, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // Check size and elitism rate
        assertEquals(15, nextGeneration.size());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);

        // Check that elitism was applied
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        int boundIndex = (int) Math.ceil((1.0 - 0.5) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i - boundIndex);
            assertEquals(oldChromosome, newChromosome);
        }
    }

    /**
     * Test nextGeneration method with only population limit and elitism rate.
     */
    @Test
    public void testNextGenerationWithOnlyPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // Check size and elitism rate
        assertEquals(15, nextGeneration.size());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);

        // Create and sort initial population
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        // Check that elitism was applied
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        int boundIndex = (int) Math.ceil((1.0 - 0.5) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i - boundIndex);
            assertEquals(oldChromosome, newChromosome);
        }
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    /**
     * Test OutOfRangeException when setting elitism rate outside [0, 1] range.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutsideRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        population.setElitismRate(-0.1);
    }
}

class StubChromosome implements Chromosome {
    private final int id;

    public StubChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenes() {
        // Not used in this test
        return new double[0];
    }

    @Override
    public double getFitness() {
        // Not used in this test
        return 0;
    }

    @Override
    public int getLength() {
        // Not used in this test
        return 0;
    }

    @Override
    public int getId() {
        return id;
    }
}