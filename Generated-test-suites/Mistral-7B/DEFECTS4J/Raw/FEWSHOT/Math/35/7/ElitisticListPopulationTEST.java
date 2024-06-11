package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for the ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes and population limit.
     */
    @Test
    public void testElitisticListPopulationConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test constructor with population limit only.
     */
    @Test
    public void testElitisticListPopulationConstructorWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.size());
    }

    /**
     * Test nextGeneration method with initial chromosomes.
     */
    @Test
    public void testNextGenerationWithInitialChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        Population nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            assertTrue(nextGeneration.contains(oldChromosomes.get(i)));
        }
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);
        population.nextGeneration();
    }

    /**
     * Test setElitismRate method.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate());
    }

    /**
     * Test setElitismRate method with invalid rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Dummy Chromosome for testing.
     */
    static class DummyChromosome implements Chromosome {
        private int id;

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