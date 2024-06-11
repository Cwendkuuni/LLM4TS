package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.abs;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test creating ElitisticListPopulation with valid parameters.
     */
    @Test
    public void testValidCreation() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test creating ElitisticListPopulation with valid population limit.
     */
    @Test
    public void testValidPopulationLimitCreation() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.size());
    }

    /**
     * Test creating ElitisticListPopulation with valid elitism rate.
     */
    @Test
    public void testValidElitismRateCreation() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }

    /**
     * Test creating ElitisticListPopulation with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testInvalidElitismRateCreation() {
        new ElitisticListPopulation(5, -0.1);
    }

    /**
     * Test setting elitism rate in ElitisticListPopulation.
     */
    @Test
    public void testSettingElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }

    /**
     * Test invalid elitism rate setting in ElitisticListPopulation.
     */
    @Test(expected = OutOfRangeException.class)
    public void testInvalidElitismRateSetting() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test nextGeneration method with valid population.
     */
    @Test
    public void testNextGenerationWithValidPopulation() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3),
                new DummyChromosome(4), new DummyChromosome(5));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        int elitismSize = (int) FastMath.ceil(0.5 * population.size());
        List<Chromosome> elitismChromosomes = population.subList(0, elitismSize);
        List<Chromosome> nonElitismChromosomes = population.subList(elitismSize, population.size());

        for (Chromosome chromosome : nextGeneration) {
            if (elitismChromosomes.contains(chromosome)) {
                assertTrue(nonElitismChromosomes.isEmpty());
            } else {
                assertFalse(elitismChromosomes.contains(chromosome));
            }
        }
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(0, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with population below elitism rate.
     */
    @Test
    public void testNextGenerationWithPopulationBelowElitismRate() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.2);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        for (Chromosome chromosome : nextGeneration) {
            assertTrue(chromosomes.contains(chromosome));
        }
    }

    /**
     * Dummy chromosome for testing.
     */
    private static class DummyChromosome implements Chromosome {
        private final int id;

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

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Chromosome)) {
                return false;
            }

            Chromosome other = (Chromosome) obj;
            return this.id == other.getId();
        }

        public int getId() {
            return id;
        }
    }
}