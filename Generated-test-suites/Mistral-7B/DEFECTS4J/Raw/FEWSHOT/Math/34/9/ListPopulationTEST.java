package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

public class ListPopulationTest {

    /**
     * Test constructor ListPopulation with initial list and population limit.
     */
    @Test
    public void testListPopulationConstructorWithListAndLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new DummyChromosome(1));
        initialChromosomes.add(new DummyChromosome(2));

        ListPopulation pop = new ListPopulation<>(initialChromosomes, 5);

        assertEquals(5, pop.getPopulationLimit());
        assertEquals(5, pop.getPopulationSize());
        assertEquals(pop.getChromosomes(), initialChromosomes);
    }

    /**
     * Test constructor ListPopulation with initial empty list and population limit.
     */
    @Test
    public void testListPopulationConstructorWithEmptyListAndLimit() {
        ListPopulation pop = new ListPopulation<>(5);

        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertTrue(pop.getChromosomes().isEmpty());
    }

    /**
     * Test constructor ListPopulation with initial list and no population limit.
     */
    @Test
    public void testListPopulationConstructorWithListAndNoLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new DummyChromosome(1));
        initialChromosomes.add(new DummyChromosome(2));

        ListPopulation pop = new ListPopulation<>(initialChromosomes);

        assertEquals(-1, pop.getPopulationLimit());
        assertEquals(initialChromosomes.size(), pop.getPopulationSize());
        assertEquals(pop.getChromosomes(), initialChromosomes);
    }

    /**
     * Test constructor ListPopulation with initial empty list and no population limit.
     */
    @Test
    public void testListPopulationConstructorWithEmptyListAndNoLimit() {
        ListPopulation pop = new ListPopulation<>();

        assertEquals(-1, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertTrue(pop.getChromosomes().isEmpty());
    }

    /**
     * Test setting chromosomes with null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);
        pop.setChromosomes(null);
    }

    /**
     * Test setting chromosomes with list size bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);
        List<Chromosome> listToAdd = new ArrayList<>();
        listToAdd.add(new DummyChromosome(1));
        listToAdd.add(new DummyChromosome(2));
        listToAdd.add(new DummyChromosome(3));
        pop.setChromosomes(listToAdd);
    }

    /**
     * Test adding chromosomes to empty list population.
     */
    @Test
    public void testAddChromosomesToEmptyListPopulation() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);

        pop.addChromosomes(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)));

        assertEquals(2, pop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(0), new DummyChromosome(1));
        assertEquals(pop.getChromosomes().get(1), new DummyChromosome(2));
    }

    /**
     * Test adding chromosomes to list population with population limit.
     */
    @Test
    public void testAddChromosomesToListPopulationWithPopulationLimit() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);

        pop.addChromosomes(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3)));

        assertEquals(3, pop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(0), new DummyChromosome(1));
        assertEquals(pop.getChromosomes().get(1), new DummyChromosome(2));
        assertEquals(pop.getChromosomes().get(2), new DummyChromosome(3));

        // check population limit not exceeded
        List<Chromosome> listToAdd = new ArrayList<>();
        listToAdd.add(new DummyChromosome(4));
        listToAdd.add(new DummyChromosome(5));
        pop.addChromosomes(listToAdd);

        assertEquals(5, pop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(3), new DummyChromosome(4));
        assertEquals(pop.getChromosomes().get(4), new DummyChromosome(5));
    }

    /**
     * Test adding chromosomes to list population with no population limit.
     */
    @Test
    public void testAddChromosomesToListPopulationWithNoPopulationLimit() {
        ListPopulation pop = new ListPopulation<>();

        pop.addChromosomes(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3)));

        assertEquals(3, pop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(0), new DummyChromosome(1));
        assertEquals(pop.getChromosomes().get(1), new DummyChromosome(2));
        assertEquals(pop.getChromosomes().get(2), new DummyChromosome(3));

        // check population limit not exceeded
        List<Chromosome> listToAdd = new ArrayList<>();
        listToAdd.add(new DummyChromosome(4));
        listToAdd.add(new DummyChromosome(5));
        listToAdd.add(new DummyChromosome(6));
        listToAdd.add(new DummyChromosome(7));
        pop.addChromosomes(listToAdd);

        assertEquals(7, pop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(3), new DummyChromosome(4));
        assertEquals(pop.getChromosomes().get(4), new DummyChromosome(5));
        assertEquals(pop.getChromosomes().get(5), new DummyChromosome(6));
        assertEquals(pop.getChromosomes().get(6), new DummyChromosome(7));
    }

    /**
     * Test getting unmodifiable list of chromosomes.
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1));
        listPopulation.add(new DummyChromosome(2));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        List<Chromosome> chromosomes = pop.getChromosomes();

        assertEquals(listPopulation, chromosomes);
        assertFalse(chromosomes.isEmpty());
        assertNull(chromosomes.get(0).getFitness());
    }

    /**
     * Test getting list of chromosomes.
     */
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1));
        listPopulation.add(new DummyChromosome(2));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        List<Chromosome> chromosomes = pop.getChromosomeList();

        assertEquals(listPopulation, chromosomes);
        assertFalse(chromosomes.isEmpty());
        assertNull(chromosomes.get(0).getFitness());
    }

    /**
     * Test adding a chromosome to population.
     */
    @Test
    public void testAddChromosome() {
        ListPopulation pop = new ListPopulation<>();

        pop.addChromosome(new DummyChromosome(1));

        List<Chromosome> chromosomes = pop.getChromosomes();

        assertEquals(1, chromosomes.size());
        assertEquals(new DummyChromosome(1), chromosomes.get(0));
    }

    /**
     * Test adding a chromosome to population with population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimit() {
        ListPopulation pop = new ListPopulation<>(Arrays.asList(new DummyChromosome(1)), 1);

        pop.addChromosome(new DummyChromosome(2));
    }

    /**
     * Test getting fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1, 5.0));
        listPopulation.add(new DummyChromosome(2, 3.0));
        listPopulation.add(new DummyChromosome(3, 4.0));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        Chromosome bestChromosome = pop.getFittestChromosome();

        assertEquals(new DummyChromosome(1, 5.0), bestChromosome);
    }

    /**
     * Test getting maximal population size.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);

        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test setting maximal population size.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation pop = new ListPopulation<>(Collections.emptyList(), 5);

        pop.setPopulationLimit(10);

        assertEquals(10, pop.getPopulationLimit());
    }

    /**
     * Test setting maximal population size with smaller current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerCurrentPopulationSize() {
        ListPopulation pop = new ListPopulation<>(Collections.singletonList(new DummyChromosome(1)), 5);

        pop.setPopulationLimit(3);
    }

    /**
     * Test getting current population size.
     */
    @Test
    public void testGetPopulationSize() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1));
        listPopulation.add(new DummyChromosome(2));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        assertEquals(listPopulation.size(), pop.getPopulationSize());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1));
        listPopulation.add(new DummyChromosome(2));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        String result = pop.toString();

        String expected = "[Chromosome[id=0, fitness=null], Chromosome[id=1, fitness=null]]";
        assertEquals(expected, result);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        List<Chromosome> listPopulation = new ArrayList<>();
        listPopulation.add(new DummyChromosome(1));
        listPopulation.add(new DummyChromosome(2));
        ListPopulation pop = new ListPopulation<>(listPopulation);

        Iterator<Chromosome> iterator = pop.iterator();

        assertEquals(new DummyChromosome(1), iterator.next());
        assertEquals(new DummyChromosome(2), iterator.next());
        fail("Expected NoSuchElementException but got next() instead");
    }

    private static class DummyChromosome implements Chromosome {
        private int id;
        private Double fitness;

        public DummyChromosome(int id) {
            this.id = id;
        }

        public DummyChromosome(int id, Double fitness) {
            this.id = id;
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Chromosome chromosome) {
            int compareFitness = this.fitness.compareTo(chromosome.getFitness());
            if (compareFitness != 0) {
                return compareFitness;
            }
            return this.id - ((DummyChromosome) chromosome).id;
        }

        @Override
        public Double getFitness() {
            return fitness;
        }

        @Override
        public String toString() {
            return "Chromosome[id=" + id + ", fitness=" + fitness + "]";
        }
    }
}