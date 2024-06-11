package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial chromosomes list and population limit.
     */
    @Test
    public void testElitisticListPopulationInitializationWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2));
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with population limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationInitializationWithPopulationLimitAndElitismRate() {
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertNull(population.getChromosomes());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test nextGeneration method with initial chromosomes list and population limit.
     */
    @Test
    public void testNextGenerationWithInitialChromosomesAndPopulationLimit() {
        List<Chromosome> initialChromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2),
                new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        int populationLimit = 7;
        double elitismRate = 0.5;

        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(initialChromosomes, populationLimit, elitismRate);

        List<Chromosome> oldChromosomes = List.copyOf(parentPopulation.getChromosomes());
        Collections.sort(oldChromosomes);

        ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.size());
        assertTrue(nextGeneration.containsAll(oldChromosomes.subList(oldChromosomes.size() - 2, oldChromosomes.size())));
        assertFalse(nextGeneration.containsAll(oldChromosomes.subList(0, oldChromosomes.size() - 2)));
    }

    /**
     * Test nextGeneration method with population limit and elitism rate.
     */
    @Test
    public void testNextGenerationWithPopulationLimitAndElitismRate() {
        int populationLimit = 7;
        double elitismRate = 0.5;

        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(populationLimit, elitismRate);

        ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.size());
        assertNull(nextGeneration.getChromosomes());
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(), 5, 0.9);

        double elitismRate = 0.7;

        population.setElitismRate(elitismRate);

        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(), 5, 0.9);

        double elitismRate = -0.1;

        population.setElitismRate(elitismRate);
    }

    /**
     * Helper class for chromosomes.
     */
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
            return "DummyChromosome{" + id + '}';
        }

    }

}