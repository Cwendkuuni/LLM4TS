package org.apache.commons.math3.genetics;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.nextUp;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructionWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithNegativeElitismRate() {
        new ElitisticListPopulation(5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(5, 1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, 5, 0.5);
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) parentPopulation.nextGeneration();
        assertEquals(5, nextGeneration.size());
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithSortedChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Collections.sort(parentPopulation.getChromosomes());
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) parentPopulation.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        Chromosome expectedFirstChromosome = nextGenerationChromosomes.get(0);
        Chromosome expectedLastChromosome = nextGenerationChromosomes.get(nextGeneration.size() - 1);

        // The first chromosome in the next generation should be the best one from the parent population
        assertEquals(nextUp(1.0), expectedFirstChromosome.getFitness(), 0.01);

        // The last chromosome in the next generation should be the second best one from the parent population
        assertEquals(nextUp(0.9), expectedLastChromosome.getFitness(), 0.01);
    }

    /**
     * Dummy implementation of a chromosome for testing purposes.
     */
    public static class DummyChromosome implements Chromosome {
        private int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" +
                    "id=" + id +
                    '}';
        }
    }

}