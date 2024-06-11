package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationLimit() {
        new ListPopulation(-10);
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroPopulationLimit() {
        new ListPopulation(0);
    }

    @Test
    public void testAddChromosomes() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(c1);
        chromosomeList.add(c2);
        population.addChromosomes(chromosomeList);
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        Chromosome c4 = new Chromosome();
        Chromosome c5 = new Chromosome();
        Chromosome c6 = new Chromosome();
        Chromosome c7 = new Chromosome();
        Chromosome c8 = new Chromosome();
        Chromosome c9 = new Chromosome();
        Chromosome c10 = new Chromosome();
        Chromosome c11 = new Chromosome();
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(c1);
        chromosomeList.add(c2);
        chromosomeList.add(c3);
        chromosomeList.add(c4);
        chromosomeList.add(c5);
        chromosomeList.add(c6);
        chromosomeList.add(c7);
        chromosomeList.add(c8);
        chromosomeList.add(c9);
        chromosomeList.add(c10);
        chromosomeList.add(c11);
        population.addChromosomes(chromosomeList);
    }

    @Test
    public void testGetChromosomes() {
        assertNotNull(population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        assertNotNull(population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        Chromosome c = new Chromosome();
        population.addChromosome(c);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        Chromosome c4 = new Chromosome();
        Chromosome c5 = new Chromosome();
        Chromosome c6 = new Chromosome();
        Chromosome c7 = new Chromosome();
        Chromosome c8 = new Chromosome();
        Chromosome c9 = new Chromosome();
        Chromosome c10 = new Chromosome();
        Chromosome c11 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        population.addChromosome(c3);
        population.addChromosome(c4);
        population.addChromosome(c5);
        population.addChromosome(c6);
        population.addChromosome(c7);
        population.addChromosome(c8);
        population.addChromosome(c9);
        population.addChromosome(c10);
        population.addChromosome(c11);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        c1.setFitness(10);
        c2.setFitness(15);
        population.addChromosome(c1);
        population.addChromosome(c2);
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(c2, fittest);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        population.setPopulationLimit(-10);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitZero() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentPopulationSize() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        Chromosome c4 = new Chromosome();
        Chromosome c5 = new Chromosome();
        Chromosome c6 = new Chromosome();
        Chromosome c7 = new Chromosome();
        Chromosome c8 = new Chromosome();
        Chromosome c9 = new Chromosome();
        Chromosome c10 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        population.addChromosome(c3);
        population.addChromosome(c4);
        population.addChromosome(c5);
        population.addChromosome(c6);
        population.addChromosome(c7);
        population.addChromosome(c8);
        population.addChromosome(c9);
        population.addChromosome(c10);
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testToString() {
        assertNotNull(population.toString());
    }
}