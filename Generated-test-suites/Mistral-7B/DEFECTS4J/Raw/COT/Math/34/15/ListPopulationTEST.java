package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-5);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        assertEquals(chromosomes.size(), population.getPopulationSize());
        for (Chromosome chromosome : chromosomes) {
            assertTrue(population.getChromosomes().contains(chromosome));
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.setChromosomes(chromosomes);

        assertEquals(chromosomes.size(), population.getPopulationSize());
        for (Chromosome chromosome : chromosomes) {
            assertTrue(population.getChromosomes().contains(chromosome));
        }

        // testing that the original list is unmodifiable
        assertFalse(chromosomes.remove(chromosomes.get(0)));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomesList() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.setChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        List<Chromosome> populationChromosomes = population.getChromosomes();

        assertEquals(chromosomes.size(), populationChromosomes.size());
        for (Chromosome chromosome : chromosomes) {
            assertTrue(populationChromosomes.contains(chromosome));
        }
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        List<Chromosome> populationChromosomes = population.getChromosomeList();

        assertEquals(chromosomes.size(), populationChromosomes.size());
        for (Chromosome chromosome : chromosomes) {
            assertTrue(populationChromosomes.contains(chromosome));
        }
    }

    @Test
    public void testAddChromosome() {
        population.addChromosome(new Chromosome());

        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitReached() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        Chromosome bestChromosome = population.getFittestChromosome();

        assertEquals(chromosomes.get(0), bestChromosome);

        population.remove(chromosomes.get(0));

        bestChromosome = population.getFittestChromosome();

        assertEquals(chromosomes.get(1), bestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithPopulationSizeBiggerThanNewPopulationLimit() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());

        population.setPopulationLimit(1);
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        String toString = population.toString();

        assertNotNull(toString);

        String expectedToString = "[";
        for (Chromosome chromosome : chromosomes) {
            expectedToString += chromosome.toString();
            expectedToString += ", ";
        }
        expectedToString += "]";

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.addChromosomes(chromosomes);

        Iterator<Chromosome> iterator = population.iterator();

        List<Chromosome> chromosomesIterator = new ArrayList<>();

        while (iterator.hasNext()) {
            chromosomesIterator.add(iterator.next());
        }

        assertEquals(chromosomes.size(), chromosomesIterator.size());
        for (int i = 0; i < chromosomes.size(); i++) {
            assertEquals(chromosomes.get(i), chromosomesIterator.get(i));
        }
    }
}