package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.junit.Test;

/**
 * Tests for the {@link ElitisticListPopulation} class.
 */
public class ElitisticListPopulationTest {

    /**
     * Tests the creation of an ElitisticListPopulation with a list of chromosomes,
     * a population limit, and a elitism rate.
     */
    @Test
    public void testElitisticListPopulationCreationWithListAndLimitAndElitismRate() {
        List<Chromosome> chromosomes = List.of(new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulationBuilder()
                .chromosomes(chromosomes)
                .populationLimit(10)
                .elitismRate(0.5)
                .build();
        assertEquals(10, population.size());
        assertEquals(chromosomes.get(0), population.getChromosome(0));
        assertEquals(chromosomes.get(1), population.getChromosome(1));
        // the next chromosomes should be the best ones from the input list
        for (int i = 2; i < chromosomes.size(); i++) {
            Chromosome bestChromosome = chromosomes.get(i);
            assertEquals(bestChromosome, population.getChromosome(i - 2));
        }
    }

    /**
     * Tests the creation of an ElitisticListPopulation with a population limit
     * and a elitism rate.
     */
    @Test
    public void testElitisticListPopulationCreationWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
        // the first chromosome is randomly created
        assertNotNull(population.getChromosome(0));
        // the next chromosomes are the best ones from the initial population
        ElitisticListPopulation initialPopulation = new ElitisticListPopulation(10, 0.5);
        List<Chromosome> chromosomes = List.copyOf(initialPopulation);
        Collections.sort(chromosomes);
        for (int i = 1; i < initialPopulation.size(); i++) {
            Chromosome bestChromosome = chromosomes.get(i);
            assertEquals(bestChromosome, population.getChromosome(i - 1));
        }
    }

    /**
     * Tests the exception thrown when the elitism rate is outside the [0, 1] range.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithInvalidElitismRate() {
        new ElitisticListPopulation(new ElitisticListPopulation(5, 0.5), 5, -0.1);
    }

    /**
     * Tests the population next generation.
     */
    @Test
    public void testPopulationNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome()), 10, 0.5);
        population.nextGeneration();
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        // the elitism rate percent of the best chromosomes are directly copied to the next generation
        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i - boundIndex);
            assertEquals(oldChromosome, newChromosome);
        }
        // the remaining chromosomes are created new
        for (int i = 0; i < boundIndex; i++) {
            assertNotSame(oldChromosomes.get(i), newChromosomes.get(i));
        }
    }

    /**
     * Tests the population elitism rate getter and setter.
     */
    @Test
    public void testPopulationElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate());
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate());
        // exception when the elitism rate is outside the [0, 1] range
        try {
            population.setElitismRate(-0.1);
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getLocalizedMessage());
            assertEquals(-0.1, e.getValue());
            assertEquals(0, e.getMinimum());
            assertEquals(1, e.getMaximum());
        }
    }
}