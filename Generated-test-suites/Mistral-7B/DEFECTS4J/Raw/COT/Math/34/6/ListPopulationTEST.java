package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tests for the ListPopulation class
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit
     */
    @Test
    public void testListPopulationConstructorValidLimit() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test constructor with invalid population limit
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorInvalidLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test setChromosomes method with null argument
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullArgument() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    /**
     * Test setChromosomes method with valid chromosomes
     */
    @Test
    public void testSetChromosomesValidChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);

        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(5, population.getPopulationSize());
    }

    /**
     * Test setChromosomes method with chromosomes exceeding population limit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
    }

    /**
     * Test addChromosomes method with null argument
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullArgument() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(null);
    }

    /**
     * Test addChromosomes method with valid chromosomes
     */
    @Test
    public void testAddChromosomesValidChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);

        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(5, population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with chromosomes exceeding population limit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
    }

    /**
     * Test getFittestChromosome method
     */
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);

        Chromosome bestChromosome = population.getFittestChromosome();
        Assert.assertEquals(bestChromosome, population.getChromosomes().get(0));
    }

    /**
     * Test getPopulationLimit method
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with invalid population limit
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-1);
    }

    /**
     * Test setPopulationLimit method with population size smaller than new population limit
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitPopulationSizeSmallerThanNewLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
    }

    /**
     * Test setPopulationLimit method with population size equal to new population limit
     */
    @Test
    public void testSetPopulationLimitPopulationSizeEqualToNewLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);

        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationSize());
    }

    /**
     * Test setPopulationLimit method with population size greater than new population limit
     */
    @Test
    public void testSetPopulationLimitPopulationSizeGreaterThanNewLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Chromosome chromosome = new Chromosome(new double[1]);
            chromosomes.add(chromosome);
        }

        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);

        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationSize());
    }
}