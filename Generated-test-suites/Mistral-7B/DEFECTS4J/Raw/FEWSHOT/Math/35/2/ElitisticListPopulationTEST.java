package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialChromosomes() {
        // given
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3),
                new DummyChromosome(4), new DummyChromosome(5));
        int populationLimit = 10;
        double elitismRate = 0.5;

        // when
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // then
        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativeElitismRate() {
        // given
        List<Chromosome> chromosomes = List.of();
        int populationLimit = 10;
        double elitismRate = -0.1;

        // when
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithElitismRateGreaterThanOne() {
        // given
        List<Chromosome> chromosomes = List.of();
        int populationLimit = 10;
        double elitismRate = 1.1;

        // when
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        // given
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // when
        Population nextGeneration = population.nextGeneration();

        // then
        assertEquals(populationLimit, nextGeneration.size());
        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * chromosomes.size());
        List<Chromosome> oldChromosomes = population.getChromosomes();
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            assertNotNull(nextGeneration.getChromosome(i));
            assertEquals(oldChromosomes.get(i), nextGeneration.getChromosome(i));
        }
    }

    @Test
    public void testSetElitismRate() {
        // given
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(), 10, 0.5);

        // when
        double newElitismRate = 0.7;
        population.setElitismRate(newElitismRate);

        // then
        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        // given
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(), 10, 0.5);

        // when
        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        // given
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(), 10, 0.5);

        // when
        double newElitismRate = 1.1;
        population.setElitismRate(newElitismRate);
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
    }
}