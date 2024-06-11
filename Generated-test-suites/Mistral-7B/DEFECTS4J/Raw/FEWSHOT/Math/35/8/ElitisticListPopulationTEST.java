package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        List<Chromosome> chromosomes = Collections.emptyList();
        int populationLimit = 10;
        double elitismRate = -0.1;
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = Collections.emptyList();
        int populationLimit = 10;
        double elitismRate = 1.1;
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ListPopulation pop = new ElitisticListPopulation(1);
        ElitisticListPopulation nextGeneration = pop.nextGeneration();
        assertTrue(nextGeneration.size() > 0);
    }

    @Test
    public void testNextGenerationWithMultipleChromosomesAndElitismRateHigherThan50() {
        List<Chromosome> chromosomes = List.of(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3), new MockChromosome(4));
        int populationLimit = chromosomes.size();
        double elitismRate = 0.8;

        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        ElitisticListPopulation nextGeneration = pop.nextGeneration();

        int bestChromosomesCount = (int) Math.ceil(elitismRate * chromosomes.size());
        int expectedSize = populationLimit - bestChromosomesCount;

        assertEquals(expectedSize, nextGeneration.size());
        for (int i = 0; i < bestChromosomesCount; i++) {
            Chromosome parent = pop.getChromosome(i);
            assertTrue(nextGeneration.contains(parent));
        }

        for (int i = bestChromosomesCount; i < chromosomes.size(); i++) {
            Chromosome child = nextGeneration.get(i);
            assertNotSame(chromosomes.get(i), child);
        }
    }

    @Test
    public void testNextGenerationWithMultipleChromosomesAndElitismRateLessThan50() {
        List<Chromosome> chromosomes = List.of(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3), new MockChromosome(4));
        int populationLimit = chromosomes.size();
        double elitismRate = 0.5;

        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        ElitisticListPopulation nextGeneration = pop.nextGeneration();

        int bestChromosomesCount = (int) Math.ceil(elitismRate * chromosomes.size());

        int expectedSize = populationLimit - bestChromosomesCount;
        assertEquals(expectedSize, nextGeneration.size());
        for (int i = 0; i < bestChromosomesCount; i++) {
            Chromosome parent = pop.getChromosome(i);
            assertTrue(nextGeneration.contains(parent));
        }

        for (int i = bestChromosomesCount; i < chromosomes.size(); i++) {
            Chromosome child = nextGeneration.get(i);
            assertNotSame(chromosomes.get(i), child);
        }

        for (int i = 0; i < bestChromosomesCount; i++) {
            Chromosome parent = pop.getChromosome(i);
            List<Chromosome> parents = nextGeneration.getSubList(0, bestChromosomesCount);
            Chromosome child = parents.get(i);
            assertNotSame(parent, child);
            assertEquals(parent.getFitness(), child.getFitness(), 0.001);
        }
    }

    @Test
    public void testSetElitismRateWithNegativeValue() {
        ElitisticListPopulation pop = new ElitisticListPopulation(1, 1.0, 0.0);
        double elitismRate = -0.1;

        try {
            pop.setElitismRate(elitismRate);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testSetElitismRateWithValueGreaterThanOne() {
        ElitisticListPopulation pop = new ElitisticListPopulation(1, 1.0, 0.0);
        double elitismRate = 1.1;

        try {
            pop.setElitismRate(elitismRate);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testSetElitismRateWithValidValue() {
        ElitisticListPopulation pop = new ElitisticListPopulation(1, 1.0, 0.0);
        double elitismRate = 0.9;

        pop.setElitismRate(elitismRate);
        assertEquals(elitismRate, pop.getElitismRate(), 0.001);
    }

    private static class MockChromosome implements Chromosome {
        private static int nextId = 1;
        private final int id;
        private final double fitness;

        public MockChromosome(double fitness) {
            this.id = nextId++;
            this.fitness = fitness;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public void evaluateFitness() {
            // no-op
        }
    }
}