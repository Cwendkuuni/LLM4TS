package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    private ListPopulation population;
    private List<Chromosome> chromosomes;

    /**
     * Initializes the test environment.
     */
    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population = new ListPopulation(chromosomes, 3);
    }

    /**
     * Tests constructor ListPopulation(int populationLimit).
     */
    @Test
    public void testListPopulationConstructorInt() {
        population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests constructor ListPopulation(List<Chromosome> chromosomes, int populationLimit).
     */
    @Test
    public void testListPopulationConstructorListInt() {
        assertEquals(2, population.getPopulationSize());
        assertEquals(3, population.getPopulationLimit());
    }

    /**
     * Tests method setChromosomes(List<Chromosome> chromosomes).
     */
    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new FakeChromosome(3));
        population.setChromosomes(newChromosomes);
        assertEquals(1, population.getPopulationSize());
        assertFalse(population.getChromosomes().contains(chromosome1));
        assertFalse(population.getChromosomes().contains(chromosome2));
        assertTrue(population.getChromosomes().contains(newChromosomes.get(0)));
    }

    /**
     * Tests method addChromosomes(Collection<Chromosome> chromosomes).
     */
    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new FakeChromosome(3));
        population.addChromosomes(newChromosomes);
        assertEquals(3, population.getPopulationSize());
        assertFalse(population.getChromosomes().contains(chromosome1));
        assertFalse(population.getChromosomes().contains(chromosome2));
        assertTrue(population.getChromosomes().contains(newChromosomes.get(0)));
        
        List<Chromosome> tooManyChromosomes = new ArrayList<>();
        tooManyChromosomes.addAll(chromosomes);
        tooManyChromosomes.add(new FakeChromosome(4));
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(tooManyChromosomes));
    }

    /**
     * Tests method getChromosomes().
     */
    @Test
    public void testGetChromosomes() {
        assertEquals(chromosomes, population.getChromosomes());
        assertNotNull(population.getChromosomes());
    }

    /**
     * Tests method getChromosomeList().
     */
    @Test
    public void testGetChromosomeList() {
        assertEquals(chromosomes, population.getChromosomeList());
        assertNotNull(population.getChromosomeList());
    }

    /**
     * Tests method addChromosome(Chromosome chromosome).
     */
    @Test
    public void testAddChromosome() {
        population.addChromosome(new FakeChromosome(3));
        assertEquals(3, population.getPopulationSize());
        assertFalse(population.getChromosomes().contains(chromosome1));
        assertFalse(population.getChromosomes().contains(chromosome2));
        assertTrue(population.getChromosomes().contains(new FakeChromosome(3)));
        
        population.addChromosome(new FakeChromosome(4));
        assertEquals(4, population.getPopulationSize());
        
        List<Chromosome> tooManyChromosomes = new ArrayList<>();
        tooManyChromosomes.add(new FakeChromosome(5));
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(tooManyChromosomes.get(0)));
    }

    /**
     * Tests method getFittestChromosome().
     */
    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1, fittestChromosome);
    }

    /**
     * Tests method getPopulationLimit().
     */
    @Test
    public void testGetPopulationLimit() {
        assertEquals(3, population.getPopulationLimit());
    }

    /**
     * Tests method setPopulationLimit(int populationLimit).
     */
    @Test
    public void testSetPopulationLimit() {
        int newPopulationLimit = 5;
        population.setPopulationLimit(newPopulationLimit);
        assertEquals(newPopulationLimit, population.getPopulationLimit());
    }

    /**
     * Tests method getPopulationSize().
     */
    @Test
    public void testGetPopulationSize() {
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests method toString().
     */
    @Test
    public void testToString() {
        String expected = "[Chromosome[1], Chromosome[2]]";
        String actual = population.toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests method iterator().
     */
    @Test
    public void testIterator() {
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome chromosome = iterator.next();
        assertTrue(chromosomes.contains(chromosome));
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor ListPopulation(int populationLimit) with negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test constructor ListPopulation(List<Chromosome> chromosomes, int populationLimit) with null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorNullList() {
        new ListPopulation(null, 5);
    }

    /**
     * Test constructor ListPopulation(List<Chromosome> chromosomes, int populationLimit) with chromosomes list
     * bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorListBiggerThanPopulationLimit() {
        List<Chromosome> list = new ArrayList<>();
        list.addAll(chromosomes);
        list.add(new FakeChromosome(4));
        new ListPopulation(list, 3);
    }
}

class FakeChromosome implements Chromosome {

    private int value;

    public FakeChromosome(int value) {
        this.value = value;
    }

    @Override
    public int getFitness() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Chromosome o) {
        if (o instanceof FakeChromosome) {
            FakeChromosome other = (FakeChromosome) o;
            return Integer.compare(value, other.value);
        }
        throw new IllegalArgumentException("Invalid chromosome");
    }
}