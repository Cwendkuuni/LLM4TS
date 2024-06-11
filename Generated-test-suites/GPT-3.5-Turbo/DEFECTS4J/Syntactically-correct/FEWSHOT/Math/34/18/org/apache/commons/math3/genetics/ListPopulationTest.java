package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test
    public void testConstructorWithPopulationLimit() {
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        new ListPopulation(-10);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        for (int i = 0; i < 11; i++) {
            population.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest = new Chromosome();
        population.addChromosome(fittest);
        population.addChromosome(new Chromosome());
        assertEquals(fittest, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidLimit() {
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithTooSmallLimit() {
        for (int i = 0; i < 6; i++) {
            population.addChromosome(new Chromosome());
        }
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        Collection<Chromosome> chromosomes = new ArrayList<>();
        for (Chromosome chromosome : population) {
            chromosomes.add(chromosome);
        }
        assertTrue(chromosomes.contains(chromosome1));
        assertTrue(chromosomes.contains(chromosome2));
    }

    // Add more test cases here...

}