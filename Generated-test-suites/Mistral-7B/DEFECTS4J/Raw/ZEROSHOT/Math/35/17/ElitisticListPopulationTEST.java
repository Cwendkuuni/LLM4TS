package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;

/**
 * Tests for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial list and population limit.
     */
    @org.junit.Test
    public void testElitisticListPopulationInitialList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertNotNull(pop);
        assertEquals(5, pop.size());
    }

    /**
     * Test constructor with population limit only.
     */
    @org.junit.Test
    public void testElitisticListPopulationPopulationLimit() {
        ElitisticListPopulation pop = new ElitisticListPopulation(3, 0.5);
        assertNotNull(pop);
        assertEquals(3, pop.size());
    }

    /**
     * Test nextGeneration method with initial list.
     */
    @org.junit.Test
    public void testElitisticListPopulationNextGenerationInitialList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Population nextGen = pop.nextGeneration();
        assertNotNull(nextGen);
        assertEquals(5, nextGen.size());
    }

    /**
     * Test nextGeneration method with empty list.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testElitisticListPopulationNextGenerationEmptyList() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        pop.nextGeneration();
    }

    /**
     * Test nextGeneration method with population limit smaller than current size.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testElitisticListPopulationNextGenerationPopulationLimitSmaller() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 2, 0.5);
        pop.nextGeneration();
    }

    /**
     * Test nextGeneration method with elitism rate outside [0, 1] range.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNextGenerationElitismRateOutOfRange() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 5, 2.0);
    }

    /**
     * Test setElitismRate method with elitism rate outside [0, 1] range.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateOutOfRange() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new ArrayList<>(), 5);
        pop.setElitismRate(2.0);
    }

    /**
     * Test setElitismRate method with elitism rate within [0, 1] range.
     */
    @org.junit.Test
    public void testElitisticListPopulationSetElitismRateInRange() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new ArrayList<>(), 5);
        pop.setElitismRate(0.5);
        assertEquals(0.5, pop.getElitismRate(), 0.001);
    }

    /**
     * Dummy chromosome for testing purposes.
     */
    private static class DummyChromosome implements Chromosome {
        private int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double[] toDoubleArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DummyChromosome other = (DummyChromosome) obj;
            return id == other.id;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }

}