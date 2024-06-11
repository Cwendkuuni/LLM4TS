package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3));
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithListLimitAndNullElitismRate() {
        List<Chromosome> chromosomes = Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3));
        int populationLimit = 5;

        assertThrows(IllegalArgumentException.class, () -> new ElitisticListPopulation(chromosomes, populationLimit, null));
    }

    @Test
    public void testConstructorWithListLimitAndInvalidElitismRate() {
        List<Chromosome> chromosomes = Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3));
        int populationLimit = 5;

        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(chromosomes, populationLimit, 1.5));
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithLimitAndInvalidElitismRate() {
        int populationLimit = 5;

        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(populationLimit, 1.5));
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertTrue(nextGeneration.isEmpty());
    }

    @Test
    public void testNextGenerationSingleChromosomePopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new MockChromosome(1)), 5, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(1, nextGeneration.size());
    }

    @Test
    public void testNextGenerationFullPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3)), 5, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertTrue(nextGeneration.size() > population.size());
        for (Chromosome chromosome : population) {
            boolean found = false;
            for (Chromosome nextChromosome : nextGeneration) {
                if (chromosome.equals(nextChromosome)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testSetElitismRateEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);

        assertThrows(IllegalStateException.class, () -> population.setElitismRate(0.8));
    }

    @Test
    public void testSetElitismRatePopulationWithOneChromosome() {
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new MockChromosome(1)), 5, 0.5);

        population.setElitismRate(0.8);

        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRatePopulationWithChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3)), 5, 0.5);

        population.setElitismRate(0.8);

        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3)), 5, 0.5);

        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.2));
    }

    private static class MockChromosome implements Chromosome {
        private int id;

        public MockChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MockChromosome mockChromosome = (MockChromosome) o;

            return id == mockChromosome.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }
}