package org.apache.commons.math3.genetics.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with chromosomes, populationLimit and elitismRate
     */
    @org.junit.Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with populationLimit and elitismRate
     */
    @org.junit.Test
    public void testElitisticListPopulationConstructorWithPopulationLimitAndElitismRate() {
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test nextGeneration method with chromosomes
     */
    @org.junit.Test
    public void testNextGenerationWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        Population nextGeneration = population.nextGeneration();

        int boundIndex = (int) FastMath.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        List<Chromosome> expectedNextGeneration = new ArrayList<Chromosome>();
        expectedNextGeneration.addAll(oldChromosomes.subList(boundIndex, oldChromosomes.size()));

        assertEquals(population.size(), nextGeneration.size());
        assertEquals(population.getElitismRate(), nextGeneration.getElitismRate(), 0.01);
        assertEquals(expectedNextGeneration, nextGeneration.getChromosomes());
    }

    /**
     * Test nextGeneration method with populationLimit and elitismRate
     */
    @org.junit.Test
    public void testNextGenerationWithPopulationLimitAndElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        int boundIndex = (int) FastMath.ceil((1.0 - population.getElitismRate()) * population.size());
        List<Chromosome> expectedNextGeneration = new ArrayList<Chromosome>();
        expectedNextGeneration.addAll(population.getChromosomes().subList(boundIndex, population.getChromosomes().size()));

        assertEquals(population.size(), nextGeneration.size());
        assertEquals(population.getElitismRate(), nextGeneration.getElitismRate(), 0.01);
        assertEquals(expectedNextGeneration, nextGeneration.getChromosomes());
    }

    /**
     * Test setElitismRate method
     */
    @org.junit.Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        population.setElitismRate(0.6);

        assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with out of range elitismRate
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        population.setElitismRate(-0.1);
    }

    /**
     * Dummy chromosome for testing purposes
     */
    private static class DummyChromosome implements Chromosome {

        private int value;

        public DummyChromosome(int value) {
            this.value = value;
        }

        @Override
        public int getFitness() {
            return this.value;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" +
                    "value=" + value +
                    '}';
        }
    }
}