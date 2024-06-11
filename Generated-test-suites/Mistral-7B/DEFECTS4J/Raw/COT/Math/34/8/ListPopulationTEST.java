package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test method for {@link ListPopulation#ListPopulation(int)} constructor.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationIntNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    /**
     * Test method for {@link ListPopulation#ListPopulation(int)} constructor.
     */
    @Test
    public void testListPopulationInt() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test method for {@link ListPopulation#ListPopulation(List, int)} constructor.
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomesList() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    /**
     * Test method for {@link ListPopulation#ListPopulation(List, int)} constructor.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(chromosomes, -1);
    }

    /**
     * Test method for {@link ListPopulation#ListPopulation(List, int)} constructor.
     */
    @Test
    public void testListPopulationListChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        Chromosome chromosome = population.getChromosomes().get(0);
        assertEquals(chromosomes.get(0), chromosome);
    }

    /**
     * Test method for {@link ListPopulation#setChromosomes(List)} method.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    /**
     * Test method for {@link ListPopulation#setChromosomes(List)} method.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        population.setChromosomes(chromosomes);
        chromosomes.add(new DummyChromosome(2));
    }

    /**
     * Test method for {@link ListPopulation#addChromosomes(Collection)} method.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(null);
    }

    /**
     * Test method for {@link ListPopulation#addChromosomes(Collection)} method.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        population.addChromosomes(chromosomes);
        chromosomes.add(new DummyChromosome(2));
    }

    /**
     * Test method for {@link ListPopulation#getChromosomes()} method.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> expected = new ArrayList<>();
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        List<Chromosome> actual = population.getChromosomes();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ListPopulation#getChromosomeList()} method.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> expected = new ArrayList<>();
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        List<Chromosome> actual = population.getChromosomeList();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ListPopulation#addChromosome(Chromosome)} method.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
    }

    /**
     * Test method for {@link ListPopulation#getFittestChromosome()} method.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(10));
        population.addChromosome(new DummyChromosome(5));
        population.addChromosome(new DummyChromosome(7));
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(population.getChromosomes().get(0), fittest);
    }

    /**
     * Test method for {@link ListPopulation#getPopulationLimit()} method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test method for {@link ListPopulation#setPopulationLimit(int)} method.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-1);
    }

    /**
     * Test method for {@link ListPopulation#setPopulationLimit(int)} method.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    /**
     * Test method for {@link ListPopulation#getPopulationSize()} method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Test method for {@link ListPopulation#toString()} method.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        assertEquals("[Chromosome 1, Chromosome 2]", population.toString());
    }

    /**
     * Test method for {@link ListPopulation#iterator()} method.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> expected = new ArrayList<>();
        expected.add(population.getChromosomes().get(0));
        expected.add(population.getChromosomes().get(1));
        Chromosome chromosome;
        while (iterator.hasNext()) {
            chromosome = iterator.next();
            expected.remove(chromosome);
        }
        assertEquals(Collections.emptyList(), expected);
    }
}

class DummyChromosome implements Chromosome {
    private int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return (double) fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.getFitness(), o.getFitness());
    }

    @Override
    public String toString() {
        return "Chromosome " + fitness;
    }
}