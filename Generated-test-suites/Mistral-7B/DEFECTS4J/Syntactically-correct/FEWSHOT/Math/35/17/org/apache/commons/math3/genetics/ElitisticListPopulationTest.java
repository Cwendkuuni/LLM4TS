package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialListAndLimit() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testElitisticListPopulationWithInitialLimit() {
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testNextGenerationWithEmptyList() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertNull(population.nextGeneration());
    }

    @Test
    public void testNextGenerationWithOneChromosome() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(1, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithMultipleChromosomes() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(5, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.1);
        assertThrows(OutOfRangeException.class, () -> population.nextGeneration());
    }

    @Test
    public void testNextGenerationWithElitismRateLessThanZero() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, -0.1);
        assertThrows(OutOfRangeException.class, () -> population.nextGeneration());
    }

    @Test
    public void testSetElitismRateWithValidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testSetElitismRateWithInvalidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
    }

    @Test
    public void testSetElitismRateWithValueGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.1));
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int getFitness() {
            return FastMath.max(0, id);
        }
    }
}