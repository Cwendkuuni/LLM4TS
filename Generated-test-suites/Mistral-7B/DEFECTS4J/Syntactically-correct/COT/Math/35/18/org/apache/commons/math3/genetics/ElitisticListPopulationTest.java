package org.apache.commons.math3.genetics.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.PopulationFactory;
import org.apache.commons.math3.genetics.SimpleChromosome;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testElitisticListPopulationValidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        chromosomes.add(new SimpleChromosome(20));
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationInvalidConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        new ElitisticListPopulation(chromosomes, 10, 2.0);
    }

    /**
     * Test nextGeneration method with valid population.
     */
    @Test
    public void testNextGenerationValidPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        chromosomes.add(new SimpleChromosome(20));
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();

        // Check size
        assertEquals(10, nextGeneration.size());

        // Check elite chromosomes
        for (int i = 0; i < 5; i++) {
            assertEquals(chromosomes.get(i), nextGeneration.getChromosome(i));
        }

        // Check non-elite chromosomes
        for (int i = 5; i < 10; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome nextGenChromosome = nextGeneration.getChromosome(i - 5);
            assertEquals(chromosome, nextGenChromosome);
        }
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test(expected = IllegalStateException.class)
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.nextGeneration();
    }

    /**
     * Test nextGeneration method with population larger than population limit.
     */
    @Test(expected = IllegalStateException.class)
    public void testNextGenerationPopulationTooLarge() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        for (int i = 0; i < 20; i++) {
            chromosomes.add(new SimpleChromosome(i * 10));
        }
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.nextGeneration();
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.001);
    }

    /**
     * Test setting elitism rate with invalid value.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.setElitismRate(-1);
    }

    /**
     * Test creating population with empty list and valid elitism rate.
     */
    @Test
    public void testCreateEmptyPopulationValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test creating population with null list and valid elitism rate.
     */
    @Test(expected = NullPointerException.class)
    public void testCreatePopulationWithNullList() {
        new ElitisticListPopulation(null, 5, 0.5);
    }

    /**
     * Test creating population with null list and invalid elitism rate.
     */
    @Test(expected = NullPointerException.class)
    public void testCreatePopulationWithNullListAndInvalidElitismRate() {
        new ElitisticListPopulation(null, 5, 2.0);
    }

    /**
     * Test creating population with list and invalid population limit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithInvalidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        new ElitisticListPopulation(chromosomes, 0, 0.5);
    }

    /**
     * Test creating population with list and invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testCreatePopulationWithInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        new ElitisticListPopulation(chromosomes, 5, 2.0);
    }

    /**
     * Test creating population with list and valid parameters.
     */
    @Test
    public void testCreatePopulationWithValidParameters() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        Population population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
    }

    /**
     * Test creating population with pre-initialized chromosomes and valid parameters.
     */
    @Test
    public void testCreatePopulationWithPreInitializedChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new SimpleChromosome(10));
        chromosomes.add(new SimpleChromosome(20));
        chromosomes.add(new SimpleChromosome(30));
        PopulationFactory factory = new SimplePopulationFactory();
        Population population = new ElitisticListPopulation(chromosomes, 5, 0.5, factory);
        assertEquals(5, population.size());
        for (int i = 0; i < 5; i++) {
            Chromosome expectedChromosome = chromosomes.get(i);
            Chromosome actualChromosome = population.getChromosome(i);
            assertEquals(expectedChromosome, actualChromosome);
        }
    }
}