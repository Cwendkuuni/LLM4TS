package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
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
    public void setup() {
        population = new ListPopulation(5);
    }

    @Test
    public void testListPopulationWithEmptyConstructor() {
        assertEquals(0, population.getPopulationSize());
        assertTrue(population.isEmpty());
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        assertEquals(2, population.getPopulationSize());
        assertFalse(population.isEmpty());
        assertEquals(chromosome1, population.getChromosome(0));
        assertEquals(chromosome2, population.getChromosome(1));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(-1);
        fail("Expected NotPositiveException");
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomesList() {
        new ListPopulation(null, 5);
        fail("Expected NullArgumentException");
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithChromosomesListBiggerThanPopulationLimit() {
        Chromosome[] chromosomes = new Chromosome[6];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome(i + 1);
        }
        new ListPopulation(Arrays.asList(chromosomes), 5);
        fail("Expected NumberIsTooLargeException");
    }

    @Test
    public void testAddChromosome() {
        population.addChromosome(new DummyChromosome(3));
        assertEquals(1, population.getPopulationSize());
        Chromosome chromosome = population.getChromosome(0);
        assertEquals(3, chromosome.getId());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesBiggerThanPopulationLimit() {
        population.addChromosomes(Collections.singletonList(new DummyChromosome(6)));
        fail("Expected NumberIsTooLargeException");
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);

        population.setChromosomes(chromosomes);

        assertEquals(2, population.getPopulationSize());
        assertFalse(population.isEmpty());
        assertEquals(chromosome1, population.getChromosome(0));
        assertEquals(chromosome2, population.getChromosome(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomesList() {
        population.setChromosomes(null);
        fail("Expected NullArgumentException");
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesBiggerThanPopulationLimit() {
        Chromosome[] chromosomes = new Chromosome[3];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome(i + 1);
        }
        population.setChromosomes(Arrays.asList(chromosomes));
        fail("Expected NumberIsTooLargeException");
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome2, fittestChromosome);

        chromosome1.setFitness(10.0);
        chromosome2.setFitness(5.0);

        fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        population.setPopulationLimit(-1);
        fail("Expected NotPositiveException");
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerPopulationSize() {
        population.addChromosome(new DummyChromosome(1));
        population.setPopulationLimit(0);
        fail("Expected NumberIsTooSmallException");
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        String expectedToString = "[" + chromosome1 + ", " + chromosome2 + "]";
        assertEquals(expectedToString, population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome chromosome = iterator.next();
        assertEquals(chromosome1, chromosome);
        assertFalse(iterator.hasNext());
    }

    private static class DummyChromosome implements Chromosome {
        private int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public int compareTo(Chromosome chromosome) {
            if (chromosome instanceof DummyChromosome) {
                return Integer.compare(this.id, ((DummyChromosome) chromosome).id);
            }
            throw new UnsupportedOperationException();
        }
    }
}