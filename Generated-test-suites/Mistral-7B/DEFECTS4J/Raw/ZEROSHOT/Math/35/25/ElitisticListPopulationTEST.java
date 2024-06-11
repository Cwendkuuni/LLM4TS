package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    /**
     * Test constructor with population limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.size());
    }

    /**
     * Test nextGeneration method with sorting.
     */
    @Test
    public void testNextGenerationWithSorting() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();

        List<Chromosome> oldChromosomes = parentPopulation.getChromosomes();
        Collections.sort(oldChromosomes);

        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        Collections.sort(newChromosomes);

        for (int i = 0; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertEquals(oldChromosome, newChromosome);
        }

        assertEquals(oldChromosomes.size() - (int) Math.ceil(oldChromosomes.size() * 0.1),
                nextGeneration.size());
    }

    /**
     * Test nextGeneration method without sorting.
     */
    @Test
    public void testNextGenerationWithoutSorting() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();

        List<Chromosome> oldChromosomes = parentPopulation.getChromosomes();

        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        int boundIndex = (int) Math.ceil((1.0 - parentPopulation.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertEquals(oldChromosome, newChromosome);
        }

        assertEquals(oldChromosomes.size() - (int) Math.ceil(oldChromosomes.size() * 0.1),
                nextGeneration.size());
    }

    /**
     * Test setElitismRate method.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with out of range exception.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(-0.1);
    }
}

class DummyChromosome implements Chromosome {

    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" + id + '}';
    }

    @Override
    public double[] toDoubleArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException();
    }
}