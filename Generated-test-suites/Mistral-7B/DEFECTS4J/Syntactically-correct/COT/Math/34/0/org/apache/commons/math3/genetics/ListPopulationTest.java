package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ListPopulation class.
 */
public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }

    /**
     * Test constructor with positive population limit.
     */
    @Test
    public void testConstructorPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test constructor with negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test addChromosomes method with valid collection.
     */
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with null collection.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        population.addChromosomes(null);
    }

    /**
     * Test addChromosomes method with collection size bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesCollectionSizeBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);
        chromosomes.add(new DummyChromosome(2));
        population.addChromosomes(chromosomes);
    }

    /**
     * Test setChromosomes method with valid list.
     */
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test setChromosomes method with null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        population.setChromosomes(null);
    }

    /**
     * Test setChromosomes method with list size bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesListSizeBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);
        chromosomes.add(new DummyChromosome(2));
        population.setChromosomes(chromosomes);
    }

    /**
     * Test getFittestChromosome method.
     */
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(10);
        Chromosome chromosome2 = new DummyChromosome(5);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        Chromosome expected = chromosome1;
        Chromosome actual = population.getFittestChromosome();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with positive population limit.
     */
    @Test
    public void testSetPopulationLimitPositive() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test setPopulationLimit method with smaller population limit.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmaller() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }

    /**
     * Test getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(0, population.getPopulationSize());
        population.addChromosome(new DummyChromosome(1));
        Assert.assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        String expected = "[Chromosome: 1, Chromosome: 2]";
        String actual = population.toString();
        Assert.assertEquals(expected, actual);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chromosome)) {
            return false;
        }
        Chromosome chromosome = (Chromosome) o;
        return getFitness() == chromosome.getFitness();
    }

    @Override
    public int hashCode() {
        return getFitness();
    }

    @Override
    public String toString() {
        return "Chromosome: " + getFitness();
    }
}