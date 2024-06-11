package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Arrays.isEmpty;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.junit.Test;

/**
 * Test for ElitisticListPopulation class
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial list and population limit
     */
    @Test
    public void testConstructorWithInitialListAndPopulationLimit() {
        List<Chromosome> initialList = createTestChromosomesList(10);
        ElitisticListPopulation population = new ElitisticListPopulation(initialList, 20, 0.5);
        assertEquals(20, population.size());
        assertEquals(initialList, population.getChromosomes());
    }

    /**
     * Test constructor with population limit and elitism rate
     */
    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        assertEquals(15, population.size());
        assertTrue(isEmpty(population.getChromosomes()));
    }

    /**
     * Test nextGeneration method with empty list
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextGenerationWithEmptyList() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.nextGeneration();
    }

    /**
     * Test nextGeneration method with list of size smaller than population limit
     */
    @Test
    public void testNextGenerationWithListSizeSmallerThanPopulationLimit() {
        List<Chromosome> initialList = createTestChromosomesList(5);
        ElitisticListPopulation population = new ElitisticListPopulation(initialList, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        Chromosome bestChromosome = initialList.get(0);
        Chromosome worstChromosome = initialList.get(4);
        assertTrue(nextGeneration.contains(bestChromosome));
        assertFalse(nextGeneration.contains(worstChromosome));
        List<Chromosome> remainingChromosomes = Collections.sublist(initialList, 1, 5);
        for (Chromosome chromosome : remainingChromosomes) {
            assertTrue(nextGeneration.contains(chromosome));
        }
    }

    /**
     * Test nextGeneration method with list of size equal to population limit
     */
    @Test
    public void testNextGenerationWithListSizeEqualToPopulationLimit() {
        List<Chromosome> initialList = createTestChromosomesList(10);
        ElitisticListPopulation population = new ElitisticListPopulation(initialList, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        Chromosome bestChromosome = initialList.get(0);
        Chromosome worstChromosome = initialList.get(5);
        assertTrue(nextGeneration.contains(bestChromosome));
        assertFalse(nextGeneration.contains(worstChromosome));
        for (Chromosome chromosome : initialList) {
            if (!chromosome.equals(bestChromosome) && !chromosome.equals(worstChromosome)) {
                assertTrue(nextGeneration.contains(chromosome));
            }
        }
    }

    /**
     * Test nextGeneration method with list bigger than population limit
     */
    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithListBiggerThanPopulationLimit() {
        List<Chromosome> initialList = createTestChromosomesList(15);
        ElitisticListPopulation population = new ElitisticListPopulation(initialList, 10, 0.5);
        population.nextGeneration();
    }

    /**
     * Test setElitismRate method with elitism rate outside [0, 1] range
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutsideRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.setElitismRate(-1);
    }

    /**
     * Test setElitismRate method with elitism rate within [0, 1] range
     */
    @Test
    public void testSetElitismRateWithElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.setElitismRate(0.2);
        assertEquals(0.2, population.getElitismRate(), 0.0);
    }

    /**
     * Create test chromosomes list of given size
     *
     * @param size size of the list
     * @return list of test chromosomes
     */
    private List<Chromosome> createTestChromosomesList(int size) {
        return Collections.nCopies(size, new DummyChromosome());
    }

    /**
     * Dummy chromosome for testing purposes
     */
    private static class DummyChromosome implements Chromosome {

        @Override
        public double[] toDoubleArray() {
            return new double[0];
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public void mutate(double mutationProbability) {}

        @Override
        public double fitness() {
            return 0;
        }

        @Override
        public String toString() {
            return "DummyChromosome{}";
        }
    }
}