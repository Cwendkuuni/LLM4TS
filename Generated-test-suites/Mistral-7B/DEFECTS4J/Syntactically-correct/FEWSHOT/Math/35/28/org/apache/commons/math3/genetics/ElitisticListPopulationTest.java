package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testCreateElitisticListPopulationWithInitialChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        assertEquals(10, population.size());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testCreateElitisticListPopulationWithPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));

        ElitisticListPopulation population = new ElitisticListPopulation(5, 10, 0.5);

        assertEquals(10, population.size());
        assertFalse(population.containsAll(chromosomes));
    }

    @Test
    public void testCreateElitisticListPopulationWithElitismRateOutOfRange() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));

        try {
            new ElitisticListPopulation(5, 0.2, 0.1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            new ElitisticListPopulation(5, 1.2, 0.5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Collections.sort(population.getChromosomes());
        int boundIndex = (int) Math.ceil((1.0 - 0.5) * population.size());

        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.size());
        for (int i = 0; i < boundIndex; i++) {
            Chromosome chromosome1 = population.getChromosome(i);
            Chromosome chromosome2 = nextGeneration.getChromosome(i);
            assertEquals(chromosome1, chromosome2);
        }

        assertNotSame(population.getChromosome(0), nextGeneration.getChromosome(0));
        assertNotSame(population.getChromosome(1), nextGeneration.getChromosome(1));
        assertNotSame(population.getChromosome(2), nextGeneration.getChromosome(2));
        assertNotSame(population.getChromosome(3), nextGeneration.getChromosome(3));
        assertNotSame(population.getChromosome(4), nextGeneration.getChromosome(4));
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        population.setElitismRate(0.1);
        assertEquals(0.1, population.getElitismRate(), 0.01);

        population.setElitismRate(0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test
    public void testAddElitismChromosomeToPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 10, 0.5);

        Chromosome newChromosome = new DummyChromosome(6);
        population.addChromosome(newChromosome);

        assertEquals(6, population.size());
        assertEquals(newChromosome, population.getChromosome(5));
    }

    @Test
    public void testGetBestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Chromosome bestChromosome = population.getBestChromosome();

        Collections.sort(population.getChromosomes());
        Chromosome expectedBestChromosome = population.getChromosome(0);

        assertEquals(expectedBestChromosome, bestChromosome);
    }

    @Test
    public void testGetWorstChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Chromosome worstChromosome = population.getWorstChromosome();

        Collections.sort(population.getChromosomes());
        Chromosome expectedWorstChromosome = population.getChromosome(population.size() - 1);

        assertEquals(expectedWorstChromosome, worstChromosome);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double doubleValue() {
            return 0.0;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Chromosome)) {
                return false;
            }
            Chromosome other = (Chromosome) obj;
            return this.id == other.hashCode();
        }

        @Override
        public String toString() {
            return String.format("Chromosome %d", id);
        }
    }
}