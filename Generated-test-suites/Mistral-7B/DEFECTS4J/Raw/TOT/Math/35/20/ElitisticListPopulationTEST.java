package org.apache.commons.math3.genetics;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;

/**
 * Tests for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {
    /**
     * Tests constructor with valid arguments.
     */
    @Test
    public void testConstructorWithValidArguments() {
        // create a population with 10 chromosomes and 20% elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(10, 2.0);
        assertEquals(10, population.size());
        assertEquals(2, population.getElitismRate(), 0.01);
    }

    /**
     * Tests constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithInvalidElitismRate() {
        // create a population with -1% elitism rate
        new ElitisticListPopulation(10, -1.0);
    }

    /**
     * Tests adding a chromosome to an empty population.
     */
    @Test
    public void testAddChromosomeToEmptyPopulation() {
        // create a new population
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);

        // add a chromosome
        Chromosome chromosome = new Chromosome(new double[] {1.0, 2.0, 3.0});
        population.addChromosome(chromosome);

        // check if the chromosome has been added
        assertTrue(population.contains(chromosome));
        assertEquals(1, population.size());
    }

    /**
     * Tests adding a duplicate chromosome to a population.
     */
    @Test
    public void testAddDuplicateChromosomeToPopulation() {
        // create a new population
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);

        // add a chromosome
        Chromosome chromosome = new Chromosome(new double[] {1.0, 2.0, 3.0});
        population.addChromosome(chromosome);

        // try to add the same chromosome again
        assertThrows(IllegalArgumentException.class, () -> {
            population.addChromosome(chromosome);
        });

        assertEquals(1, population.size());
    }

    /**
     * Tests getting the elitism rate.
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Tests setting the elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    /**
     * Tests setting the elitism rate with an invalid value.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Tests the next generation method with a population of size 1.
     */
    @Test
    public void testNextGenerationWithPopulationSize1() {
        // create a population of size 1
        ElitisticListPopulation population = new ElitisticListPopulation(1, 0.5);
        List<Chromosome> chromosomes = population.getChromosomes();
        Chromosome chromosome = chromosomes.get(0);

        // create a new population with the same elitism rate
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // check if the old chromosome has been copied to the new population
        assertTrue(nextGeneration.contains(chromosome));
        assertEquals(1, nextGeneration.size());
    }

    /**
     * Tests the next generation method with a population of size > 1.
     */
    @Test
    public void testNextGenerationWithPopulationSizeGreater1() {
        // create a population of size 3 with elitism rate 0.5
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);

        // sort the chromosomes by fitness
        Collections.sort(population.getChromosomes());

        // get the best chromosome
        Chromosome bestChromosome = population.getChromosomes().get(0);

        // create a new population with the same elitism rate
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // check if the best chromosome has been copied to the new population
        assertTrue(nextGeneration.contains(bestChromosome));

        // check if the size of the new population is greater than or equal to the size of the old population
        int oldSize = population.size();
        int newSize = nextGeneration.size();
        assertTrue(newSize >= oldSize);

        // check if the best chromosome is still the best in the new population
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        Chromosome newBestChromosome = newChromosomes.get(0);
        assertEquals(bestChromosome, newBestChromosome);
    }
}