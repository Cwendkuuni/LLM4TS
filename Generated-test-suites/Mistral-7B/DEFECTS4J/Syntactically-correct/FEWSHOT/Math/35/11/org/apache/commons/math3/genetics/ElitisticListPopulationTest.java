package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {
    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = createChromosomes();
        int populationLimit = 10;
        double elitismRate = 0.5;
        population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(population.size(), nextGeneration.size());

        int elitismSize = (int) FastMath.ceil(population.size() * population.getElitismRate());
        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        for (int i = 0; i < elitismSize; i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertEquals(oldChromosome, newChromosome);
        }

        for (int i = elitismSize; i < population.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i + elitismSize);
            assertNull(oldChromosome);
            assertNull(newChromosome);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.1);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.25);
        assertEquals(0.25, population.getElitismRate(), 0.01);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    private List<Chromosome> createChromosomes() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Chromosome chromosome = new MockChromosome(i);
            chromosomes.add(chromosome);
        }
        return chromosomes;
    }

    private static class MockChromosome implements Chromosome {
        private final int id;

        public MockChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Object o) {
            MockChromosome other = (MockChromosome) o;
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return Integer.toString(this.id);
        }
    }
}