package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    @org.junit.Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new DummyChromosome(1));
        initialChromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    @org.junit.Test
    public void testElitisticListPopulationConstructionWithLimitAndRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.size());
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, -0.1);
    }

    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 1.1);
    }

    @org.junit.Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assertEquals(0, population.nextGeneration().size());
    }

    @org.junit.Test
    public void testNextGenerationWithPopulationSmallerThanElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.7);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        Population nextGeneration = population.nextGeneration();
        assertEquals(2, nextGeneration.size());
    }

    @org.junit.Test
    public void testNextGenerationWithPopulationBiggerThanElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(5));
        Population nextGeneration = population.nextGeneration();
        assertEquals(3, nextGeneration.size());
    }

    @org.junit.Test
    public void testNextGenerationSorting() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(2));
        population.nextGeneration();
        List<Chromosome> sortedChromosomes = new ArrayList<>(population.getChromosomes());
        Collections.sort(sortedChromosomes);
        List<Chromosome> nextGenerationChromosomes = new ArrayList<>(population.nextGeneration().getChromosomes());
        for (int i = 0; i < sortedChromosomes.size(); i++) {
            assertEquals(sortedChromosomes.get(i), nextGenerationChromosomes.get(i));
        }
    }

    @org.junit.Test
    public void testSetElitismRateWithInvalidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        try {
            population.setElitismRate(-0.1);
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetElitismRateWithValidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    private static class DummyChromosome implements Chromosome {

        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}