package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for ElitisticListPopulation class
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes and population limit.
     */
    @Test
    public void testElitisticListPopulationWithInitialChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.nCopies(10, new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        assertEquals(15, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with population limit only.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimitOnly() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        assertEquals(15, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test next generation with initial chromosomes and population limit.
     */
    @Test
    public void testNextGenerationWithInitialChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.nCopies(10, new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.001);
    }

    /**
     * Test next generation with only population limit.
     */
    @Test
    public void testNextGenerationWithOnlyPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.001);
    }

    /**
     * Test elitism rate setting and getting.
     */
    @Test
    public void testElitismRateSettingAndGetting() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    /**
     * Test elitism rate out of range exception.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOutOfRangeException() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test sorting and elitism in next generation.
     */
    @Test
    public void testSortingAndElitismInNextGeneration() {
        List<Chromosome> chromosomes = Collections.nCopies(15, new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();
        Collections.sort(nextGeneration);
        int index = 0;
        for (Chromosome chromosome : chromosomes) {
            if (nextGeneration.get(index) == chromosome) {
                assertTrue(nextGeneration.getFitness(index) <= nextGeneration.getFitness(index + 1));
                index++;
            }
        }
        assertFalse(nextGeneration.get(index) instanceof Chromosome);
    }

    /**
     * Dummy chromosome for testing.
     */
    static class DummyChromosome implements Chromosome {
        private final int value;

        public DummyChromosome(int value) {
            this.value = value;
        }

        @Override
        public double getFitness() {
            return FastMath.pow(value, 2.0);
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + value + '}';
        }
    }
}