package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for ElitisticListPopulation class
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        assertEquals(5, population.size());
    }

    /**
     * Test nextGeneration method with valid elitism rate.
     */
    @Test
    public void testNextGenerationWithValidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        population.add(new DummyChromosome(1));
        population.add(new DummyChromosome(2));
        population.add(new DummyChromosome(3));
        population.add(new DummyChromosome(4));
        population.add(new DummyChromosome(5));
        population.add(new DummyChromosome(6));
        population.add(new DummyChromosome(7));
        population.add(new DummyChromosome(8));
        population.add(new DummyChromosome(9));
        population.add(new DummyChromosome(10));

        Collections.sort(population);

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        List<Chromosome> expectedNextGeneration = new ArrayList<>();
        expectedNextGeneration.add(population.get(population.size() - 1));
        expectedNextGeneration.add(population.get(population.size() - 2));
        expectedNextGeneration.add(population.get(population.size() - 3));
        expectedNextGeneration.add(population.get(population.size() - 4));
        expectedNextGeneration.add(population.get(population.size() - 5));

        assertEquals(5, nextGeneration.size());
        assertTrue(nextGeneration.containsAll(expectedNextGeneration));
    }

    /**
     * Test nextGeneration method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 2);
        population.add(new DummyChromosome(1));
        population.add(new DummyChromosome(2));
        population.add(new DummyChromosome(3));
        population.add(new DummyChromosome(4));
        population.add(new DummyChromosome(5));

        population.nextGeneration();
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(1.1);
    }

    /**
     * Dummy chromosome for testing purposes.
     */
    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome " + id;
        }
    }
}