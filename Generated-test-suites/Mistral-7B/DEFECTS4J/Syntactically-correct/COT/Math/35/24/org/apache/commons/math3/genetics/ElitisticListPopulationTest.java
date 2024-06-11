package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = createMockChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.9);
        assertEquals(20, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.9);
        assertEquals(15, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test nextGeneration method with initial population.
     */
    @Test
    public void testNextGenerationWithInitialPopulation() {
        List<Chromosome> chromosomes = createMockChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.9);
        Population nextGeneration = population.nextGeneration();

        assertEquals(20, nextGeneration.getPopulationLimit());
        assertEquals(1.8, nextGeneration.getElitismRate(), 0.01);

        List<Chromosome> nextChromosomes = nextGeneration.getChromosomes();
        for (int i = 0; i < nextGeneration.getPopulationLimit() / 2; i++) {
            Chromosome oldChromosome = chromosomes.get(i);
            Chromosome nextChromosome = nextChromosomes.get(i);
            assertEquals(oldChromosome, nextChromosome);
        }

        int boundIndex = (int) Math.ceil((1.0 - 0.9) * chromosomes.size());
        for (int i = boundIndex; i < chromosomes.size(); i++) {
            Chromosome oldChromosome = chromosomes.get(i);
            assertNotEquals(oldChromosome, nextChromosomes.get(i));
        }
    }

    /**
     * Test nextGeneration method with sorted population.
     */
    @Test
    public void testNextGenerationWithSortedPopulation() {
        List<Chromosome> chromosomes = createMockSortedChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.9);
        Population nextGeneration = population.nextGeneration();

        List<Chromosome> nextChromosomes = nextGeneration.getChromosomes();
        for (int i = 0; i < nextGeneration.getPopulationLimit() / 2; i++) {
            Chromosome oldChromosome = chromosomes.get(i);
            Chromosome nextChromosome = nextChromosomes.get(i);
            assertEquals(oldChromosome, nextChromosome);
        }

        int boundIndex = (int) Math.ceil((1.0 - 0.9) * chromosomes.size());
        for (int i = boundIndex; i < chromosomes.size(); i++) {
            Chromosome oldChromosome = chromosomes.get(i);
            assertNotEquals(oldChromosome, nextChromosomes.get(i));
        }
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(-0.1);
    }

    /**
     * Creates mock chromosomes.
     *
     * @param number number of chromosomes
     * @return list of mock chromosomes
     */
    private List<Chromosome> createMockChromosomes(int number) {
        return Collections.nCopies(number, createMockChromosome());
    }

    /**
     * Creates a mock chromosome.
     * @return mock chromosome
     */
    private Chromosome createMockChromosome() {
        return new MockChromosome();
    }

    /**
     * Creates sorted mock chromosomes.
     *
     * @param number number of chromosomes
     * @return list of sorted mock chromosomes
     */
    private List<Chromosome> createMockSortedChromosomes(int number) {
        List<Chromosome> chromosomes = createMockChromosomes(number);
        Collections.sort(chromosomes);
        return chromosomes;
    }
}

class MockChromosome implements Chromosome {
    // empty mock implementation
}