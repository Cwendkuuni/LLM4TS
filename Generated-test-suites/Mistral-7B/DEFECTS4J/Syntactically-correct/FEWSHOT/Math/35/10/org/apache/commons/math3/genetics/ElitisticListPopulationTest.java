package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.nCopies(10, new FakeChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        assertEquals(15, population.size());
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test constructor with population limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        assertEquals(15, population.size());
    }

    /**
     * Test next generation with empty list.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextGenerationWithEmptyList() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 15, 0.5);
        population.nextGeneration();
    }

    /**
     * Test next generation with list of size 1.
     */
    @Test
    public void testNextGenerationWithListOfSizeOne() {
        List<Chromosome> chromosomes = Collections.singletonList(new FakeChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(14, nextGeneration.size());
        assertNotNull(nextGeneration.getChromosomes().get(0));
    }

    /**
     * Test next generation with list of size 2.
     */
    @Test
    public void testNextGenerationWithListOfSizeTwo() {
        List<Chromosome> chromosomes = Collections.nCopies(2, new FakeChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(13, nextGeneration.size());
        assertNotNull(nextGeneration.getChromosomes().get(0));
    }

    /**
     * Test next generation with list of size 3.
     */
    @Test
    public void testNextGenerationWithListOfSizeThree() {
        List<Chromosome> chromosomes = Collections.nCopies(3, new FakeChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(12, nextGeneration.size());
        assertNotNull(nextGeneration.getChromosomes().get(0));
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSettingElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.nCopies(10, new FakeChromosome(0)), 15, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate());
    }

    /**
     * Test setting elitism rate with out of range value.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSettingElitismRateWithOutOfRangeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.nCopies(10, new FakeChromosome(0)), 15, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test setting elitism rate with value greater than 1.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSettingElitismRateWithValueGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.nCopies(10, new FakeChromosome(0)), 15, 0.5);
        population.setElitismRate(1.2);
    }

    /**
     * Fake chromosome for testing.
     */
    private static class FakeChromosome implements Chromosome {
        private final int value;

        public FakeChromosome(int value) {
            this.value = value;
        }

        @Override
        public double getFitness() {
            return value;
        }

        @Override
        public String toString() {
            return "FakeChromosome{" + value + '}';
        }
    }
}