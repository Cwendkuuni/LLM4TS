package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

/**
 * JUnit test file for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testElitisticListPopulationConstructorValidInput() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.8;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.2;
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Test nextGeneration method with empty chromosomes list.
     */
    @Test
    public void testNextGenerationEmptyChromosomesList() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assert (population.size() == 0);
        Population nextGeneration = population.nextGeneration();
        assert (nextGeneration.size() == 0);
    }

    /**
     * Test nextGeneration method with valid input.
     */
    @Test
    public void testNextGenerationValidInput() {
        ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 20, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }
    }

    /**
     * Test setElitismRate method with valid input.
     */
    @Test
    public void testSetElitismRateValidInput() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.6);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid input.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidInput() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.6);
        population.setElitismRate(1.2);
    }

    /**
     * Test getElitismRate method.
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.6);
        assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    /**
     * Creates a list of 10 chromosomes for testing.
     *
     * @return a list of 10 chromosomes
     */
    private List<Chromosome> createChromosomesList(int size) {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        return chromosomes;
    }

    /**
     * A dummy chromosome for testing.
     */
    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return this.id;
        }

        @Override
        public String toString() {
            return "Chromosome " + this.id;
        }
    }
}