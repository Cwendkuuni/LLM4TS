package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with correct arguments.
     */
    @Test
    public void testConstructorWithCorrectArguments() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test constructor with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithIncorrectElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 2);
    }

    /**
     * Test nextGeneration method with correct arguments.
     */
    @Test
    public void testNextGenerationWithCorrectArguments() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 0.5);
        pop.nextGeneration();
        ElitisticListPopulation nextGen = (ElitisticListPopulation) pop.getNextGeneration();
        assertEquals(5, nextGen.getPopulationLimit());
        assertEquals(0.5, nextGen.getElitismRate(), 0.01);
        assertEquals(2, nextGen.size());
        assertTrue(nextGen.containsAll(pop.getChromosomes()));
    }

    /**
     * Test nextGeneration method with incorrect size of the next generation.
     */
    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithIncorrectPopulationLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 0.5);
        pop.nextGeneration(4);
    }

    /**
     * Test nextGeneration method with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithIncorrectElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 2);
        pop.nextGeneration();
    }

    /**
     * Test setElitismRate method with correct arguments.
     */
    @Test
    public void testSetElitismRateWithCorrectArguments() {
        ElitisticListPopulation pop = new ElitisticListPopulation(5, 0.5);
        pop.setElitismRate(0.8);
        assertEquals(0.8, pop.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithIncorrectElitismRate() {
        ElitisticListPopulation pop = new ElitisticListPopulation(5, 0.5);
        pop.setElitismRate(2);
    }

    /**
     * Test empty constructor.
     */
    @Test
    public void testEmptyConstructor() {
        ElitisticListPopulation pop = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0.5, pop.getElitismRate(), 0.01);
        assertTrue(pop.isEmpty());
    }

    /**
     * Test builder with correct arguments.
     */
    @Test
    public void testBuilderWithCorrectArguments() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.size(5)
                .elitismRate(0.5)
                .build();
        ElitisticListPopulation pop = builder.build();
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0.5, pop.getElitismRate(), 0.01);
        assertTrue(pop.isEmpty());
    }

    /**
     * Test builder with empty list.
     */
    @Test
    public void testBuilderWithEmptyList() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.chromosomes(new java.util.ArrayList<>())
                .build();
        ElitisticListPopulation pop = builder.build();
        assertEquals(0, pop.getPopulationLimit());
        assertTrue(pop.isEmpty());
    }

    /**
     * Test builder with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testBuilderWithIncorrectElitismRate() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.chromosomes(new java.util.ArrayList<Chromosome>())
                .populationLimit(5)
                .elitismRate(2)
                .build();
    }

    /**
     * Test builder with incorrect population limit.
     */
    @Test(expected = OutOfRangeException.class)
    public void testBuilderWithIncorrectPopulationLimit() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.chromosomes(new java.util.ArrayList<Chromosome>())
                .populationLimit(-5)
                .elitismRate(0.5)
                .build();
    }

    /**
     * Test sortChromosomes method.
     */
    @Test
    public void testSortChromosomes() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(0));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        population.sortChromosomes();

        List<Chromosome> sortedChromosomes = population.getChromosomes();
        assertTrue(sortedChromosomes.get(0) instanceof DummyChromosome && sortedChromosomes.get(0).getFitness() > 1.0);
        assertTrue(sortedChromosomes.get(1) instanceof DummyChromosome && sortedChromosomes.get(1).getFitness() > 0.0);
        assertTrue(sortedChromosomes.get(2) instanceof DummyChromosome && sortedChromosomes.get(2).getFitness() <= 0.0);
    }

    /**
     * Dummy chromosome for testing.
     */
    private static class DummyChromosome implements Chromosome {
        private int id;
        private double fitness;

        public DummyChromosome(int id) {
            this.id = id;
            this.fitness = FastMath.random();
        }

        @Override
        public double getFitness() {
            return this.fitness;
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public String toString() {
            return String.valueOf(this.id);
        }
    }
}