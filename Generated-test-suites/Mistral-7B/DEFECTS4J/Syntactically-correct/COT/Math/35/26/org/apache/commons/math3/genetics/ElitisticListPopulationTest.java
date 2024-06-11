package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with List and population limit.
     */
    @org.junit.Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] { new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3) });
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test constructor with population limit.
     */
    @org.junit.Test
    public void testElitisticListPopulationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.size());
    }

    /**
     * Test nextGeneration method with a sorted list of chromosomes.
     */
    @org.junit.Test
    public void testNextGenerationWithSortedChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] { new DummyChromosome(1), new DummyChromosome(3), new DummyChromosome(2), new DummyChromosome(4), new DummyChromosome(5) });
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        List<Chromosome> nextGeneration = population.nextGeneration();

        // The first 3 chromosomes are the best, so they should be copied.
        List<Chromosome> expectedChromosomes = Arrays.asList(new Chromosome[] { new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5) });
        assertEquals(expectedChromosomes, nextGeneration.subList(0, 3));

        // The rest of the chromosomes should be randomly selected from the population.
        for (int i = 3; i < nextGeneration.size(); i++) {
            Chromosome chromosome = nextGeneration.get(i);
            assertTrue(population.contains(chromosome));
            assertFalse(nextGeneration.get(i).equals(nextGeneration.get(i - 3)));
        }
    }

    /**
     * Test nextGeneration method with an unsorted list of chromosomes.
     */
    @org.junit.Test
    public void testNextGenerationWithUnsortedChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] { new DummyChromosome(3), new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(4), new DummyChromosome(5) });
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        List<Chromosome> nextGeneration = population.nextGeneration();

        // The first 3 chromosomes are the best, so they should be copied.
        List<Chromosome> expectedChromosomes = Arrays.asList(new Chromosome[] { new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3) });
        Arrays.sort(nextGeneration.toArray(new Chromosome[nextGeneration.size()]));
        assertEquals(expectedChromosomes, Arrays.asList(nextGeneration.subList(0, 3)));

        // The rest of the chromosomes should be randomly selected from the population.
        for (int i = 3; i < nextGeneration.size(); i++) {
            Chromosome chromosome = nextGeneration.get(i);
            assertTrue(population.contains(chromosome));
            assertFalse(nextGeneration.get(i).equals(nextGeneration.get(i - 3)));
        }
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @org.junit.Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new DummyChromosome[0], 10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new DummyChromosome[0], 10, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test getElitismRate method.
     */
    @org.junit.Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new DummyChromosome[0], 10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.001);
    }

}

class DummyChromosome implements Chromosome {
    private final double fitness;

    public DummyChromosome(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DummyChromosome && this.fitness == ((DummyChromosome) obj).fitness;
    }

    @Override
    public int hashCode() {
        return (int) (this.fitness * 1000000);
    }
}