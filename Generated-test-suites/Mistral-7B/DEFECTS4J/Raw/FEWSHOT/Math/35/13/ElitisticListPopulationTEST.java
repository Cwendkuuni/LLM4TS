package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testElitisticListPopulationBasic() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3),
                new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
        assertEquals(nextGeneration.get(0), chromosomes.get(5));
        assertEquals(nextGeneration.get(1), chromosomes.get(4));
        assertEquals(nextGeneration.get(2), chromosomes.get(3));
        assertEquals(nextGeneration.get(3), chromosomes.get(2));
        assertEquals(nextGeneration.get(4), chromosomes.get(1));
        // the rest should be random
        assertNotEquals(nextGeneration.get(5), chromosomes.get(0));
        assertNotEquals(nextGeneration.get(6), chromosomes.get(0));
        assertNotEquals(nextGeneration.get(5), nextGeneration.get(6));
    }

    @Test
    public void testElitisticListPopulationEmpty() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.5);
        assertNull(population);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1));
        new ElitisticListPopulation(chromosomes, 1, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1));
        new ElitisticListPopulation(chromosomes, 1, 1.1);
    }

    @Test
    public void testElitisticListPopulationGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(new DummyChromosome(1)), 10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(new DummyChromosome(1)), 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
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
            return "DummyChromosome " + id;
        }
    }

}