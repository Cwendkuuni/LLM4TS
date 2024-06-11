package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import org.apache.commons.cli.Util;

import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructionWithEmptyList() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.isEmpty());
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testListPopulationConstructionWithList() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);

        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);
        assertEquals(2, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructionWithNullList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructionWithNegativePopulationLimit() {
        new ListPopulation(Collections.emptyList(), -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructionWithListBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, new Chromosome(new int[3]));
        new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);

        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2));

        assertEquals(2, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(Arrays.asList(new Chromosome(new int[1]), new Chromosome(new int[2]), new Chromosome(new int[3])));
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(5);

        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));

        assertEquals(2, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Arrays.asList(new Chromosome(new int[1]), new Chromosome(new int[2]), new Chromosome(new int[3])));
    }

    @Test
    public void testGetChromosomes() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testGetChromosomeList() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        List<Chromosome> chromosomes = population.getChromosomeList();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        ListPopulation population = new ListPopulation(5);

        population.addChromosome(chromosome1);

        assertEquals(1, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse(chromosomes.isEmpty());
        assertEquals(chromosome1, chromosomes.get(0));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeWithNullChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitReached() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        ListPopulation population = new ListPopulation(5);

        population.addChromosome(chromosome1);
        population.addChromosome(new Chromosome(new int[2]));

        try {
            population.addChromosome(new Chromosome(new int[3]));
        } catch (NumberIsTooLargeException e) {
            // expected
            return;
        }

        fail("Expected NumberIsTooLargeException");
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        Chromosome chromosome3 = new Chromosome(new int[3]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2, chromosome3), 5);

        assertEquals(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);

        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);

        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        assertEquals("[Chromosome: state=[], Chromosome: state=[]]", population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome(new int[1]);
        Chromosome chromosome2 = new Chromosome(new int[2]);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        List<Chromosome> chromosomes = new ArrayList<>(population);
        Iterator<Chromosome> iterator = population.iterator();

        List<Chromosome> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        assertEquals(chromosomes, result);
    }
}