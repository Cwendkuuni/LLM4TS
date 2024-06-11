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
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;

import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with population limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Test next generation creation with empty list.
     */
    @Test
    public void testNextGenerationWithEmptyList() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 5, 0.5);
        assertNull(population.nextGeneration());
    }

    /**
     * Test next generation creation with list of one chromosome.
     */
    @Test
    public void testNextGenerationWithListOfOneChromosome() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)));
        ElitisticListPopulation elitisticPopulation = new ElitisticListPopulation(population, 5, 0.5);
        ElitisticListPopulation nextGeneration = elitisticPopulation.nextGeneration();
        assertEquals(5, nextGeneration.getPopulationSize());
        assertTrue(nextGeneration.getChromosomes().contains(population.getChromosomes().get(0)));
    }

    /**
     * Test next generation creation with list of multiple chromosomes.
     */
    @Test
    public void testNextGenerationWithListOfMultipleChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.getPopulationSize());
        for (int i = 0; i < 5; i++) {
            if (i < 2) {
                assertTrue(nextGeneration.getChromosomes().get(i).equals(population.getChromosomes().get(i)));
            } else {
                assertFalse(nextGeneration.getChromosomes().get(i).equals(population.getChromosomes().get(i)));
            }
        }
    }

    /**
     * Test setting and getting elitism rate.
     */
    @Test
    public void testSettingAndGettingElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    /**
     * Test setting elitism rate out of range.
     */
    @Test
    public void testSettingElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.2));
    }

    /**
     * Test sorting chromosomes.
     */
    @Test
    public void testSortingChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(3), new DummyChromosome(1), new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        List<Chromosome> sortedChromosomes = population.getChromosomes();
        assertEquals(3, sortedChromosomes.get(0).getFitness());
        assertEquals(1, sortedChromosomes.get(1).getFitness());
        assertEquals(2, sortedChromosomes.get(2).getFitness());
    }

    /**
     * Test calculating bound index.
     */
    @Test
    public void testCalculatingBoundIndex() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(3), new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(5), new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        double elitismRate = population.getElitismRate();
        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * chromosomes.size());
        assertEquals(4, boundIndex);
    }

    /**
     * Test creating new population with bound index.
     */
    @Test
    public void testCreatingNewPopulationWithBoundIndex() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(3), new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(5), new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        List<Chromosome> oldChromosomes = population.getChromosomes();
        double elitismRate = population.getElitismRate();
        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * oldChromosomes.size());
        ElitisticListPopulation nextGeneration = new ElitisticListPopulation(population.getPopulationLimit(), elitismRate);
        for (int i = 0; i < boundIndex; i++) {
            nextGeneration.addChromosome(oldChromosomes.get(i));
        }
        assertEquals(5, nextGeneration.getPopulationSize());
        for (int i = 0; i < boundIndex; i++) {
            assertTrue(nextGeneration.getChromosomes().get(i).equals(population.getChromosomes().get(i)));
        }
    }
}

class DummyChromosome implements Chromosome {

    private final int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" + "fitness=" + fitness + '}';
    }
}