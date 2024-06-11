package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ExceptionUtils;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 2.5);
    }

    /**
     * Test nextGeneration method with valid population.
     */
    @Test
    public void testNextGenerationValidPopulation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.8);
        Population nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.getPopulationLimit());
        assertEquals(0.8, nextGeneration.getElitismRate(), 0.01);
        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            Chromosome oldChromosome = population.getChromosome(i);
            Chromosome newChromosome = nextGeneration.getChromosome(i);
            assertEquals(oldChromosome, newChromosome);
        }
        for (int i = 5; i < population.size(); i++) {
            Chromosome oldChromosome = population.getChromosome(i);
            assertThrows(UnsupportedOperationException.class, () -> nextGeneration.getChromosome(i));
        }
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 1, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.01);
        assertEquals(0, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with population limit less than the number of chromosomes.
     */
    @Test(expected = IllegalStateException.class)
    public void testNextGenerationPopulationLimitLessThanNumberOfChromosomes() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        population.nextGeneration();
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.5);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.5);
        population.setElitismRate(2.5);
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
        public double[] getGenotype() {
            return new double[]{id};
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return new DummyChromosome(this.id);
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o instanceof DummyChromosome) {
                return FastMath.compareTo(this.id, ((DummyChromosome) o).id);
            }
            throw new UnsupportedOperationException();
        }
    }

}