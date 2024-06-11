package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1));
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testElitisticListPopulationConstructorWithPopulationLimitAndElitismRate() {
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6));
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        Population nextGeneration = parentPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, nextGeneration.getElitismRate());
        assertTrue(nextGeneration.size() >= populationLimit);

        ElitisticListPopulation nextGenPopulation = (ElitisticListPopulation) nextGeneration;

        List<Chromosome> oldChromosomes = parentPopulation.getChromosomes();
        List<Chromosome> newChromosomes = nextGenPopulation.getChromosomes();

        Collections.sort(oldChromosomes);
        Collections.sort(newChromosomes);

        int boundIndex = (int) Math.ceil((1.0 - elitismRate) * oldChromosomes.size());

        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i - boundIndex);
            assertEquals(oldChromosome, newChromosome);
        }

        for (int i = 0; i < boundIndex; i++) {
            assertFalse(oldChromosomes.get(i).equals(newChromosomes.get(i)));
        }
    }

    @Test
    public void testSetElitismRate() {
        int populationLimit = 5;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(0.75);

        assertEquals(0.75, population.getElitismRate());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        int populationLimit = 5;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        int populationLimit = 5;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(1.1);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return 0.0;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }
}