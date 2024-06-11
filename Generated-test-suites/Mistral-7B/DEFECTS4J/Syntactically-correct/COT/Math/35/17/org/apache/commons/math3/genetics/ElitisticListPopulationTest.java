package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;

/**
 * ElitisticListPopulation tests.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @org.junit.Test
    public void testElitisticListPopulationValidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationInvalidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.1;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @org.junit.Test
    public void testElitisticListPopulationEmptyNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with non-empty population.
     */
    @org.junit.Test
    public void testElitisticListPopulationNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 10, 0.9);
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new DummyChromosome(i));
        }
        Population nextGeneration = population.nextGeneration();

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) Math.ceil((1.0 - 0.9) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome nextGenerationChromosome = nextGeneration.getChromosome(i - boundIndex);
            assertEquals(oldChromosome, nextGenerationChromosome);
        }
    }

    /**
     * Test setElitismRate method with valid argument.
     */
    @org.junit.Test
    public void testElitisticListPopulationSetElitismRateValid() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid argument.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateInvalid() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(1.1);
    }

    /**
     * Dummy chromosome for testing purposes.
     */
    public static class DummyChromosome implements Chromosome {
        private final int id;

        /**
         * Constructor.
         * @param id the chromosome id
         */
        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + id + '}';
        }
    }

}