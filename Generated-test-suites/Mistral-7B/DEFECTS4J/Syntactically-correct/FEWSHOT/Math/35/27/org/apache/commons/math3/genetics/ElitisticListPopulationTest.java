package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes and population limit
     */
    @Test
    public void testElitisticListPopulationInitializationWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test constructor with population limit and elitism rate
     */
    @Test
    public void testElitisticListPopulationInitializationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.size());
    }

    /**
     * Test nextGeneration method with initial chromosomes and population limit
     */
    @Test
    public void testNextGenerationWithInitialChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertEquals(5, population.size());
    }

    /**
     * Test nextGeneration method with population limit and elitism rate
     */
    @Test
    public void testNextGenerationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertEquals(10, population.size());
    }

    /**
     * Test setElitismRate method with valid elitism rate
     */
    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid elitism rate
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test getElitismRate method
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Dummy chromosome for testing purposes
     */
    static class DummyChromosome implements Chromosome {
        private int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return 1;
        }
    }
}