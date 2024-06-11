package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for the {Link ListPopulation} class.
 */
public class ListPopulationTest {

    private ListPopulation population;
    private List<Chromosome> initialChromosomes;

    @Before
    public void setUp() {
        initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        population = new ListPopulation<>(initialChromosomes, 5);
    }

    /**
     * Test the constructor with a valid population limit.
     */
    @Test
    public void testConstructorWithValidPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test the constructor with a population limit smaller than the number of chromosomes.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithPopulationLimitSmallerThanCurrentSize() {
        population = new ListPopulation<>(initialChromosomes, 3);
    }

    /**
     * Test the constructor with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        population = new ListPopulation<>(initialChromosomes, -1);
    }

    /**
     * Test the constructor with a null collection of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        population = new ListPopulation<>(null, 5);
    }

    /**
     * Test the method addChromosomes with a valid collection.
     */
    @Test
    public void testAddChromosomesWithValidCollection() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new DummyChromosome(3));
        population.addChromosomes(newChromosomes);
        assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test the method addChromosomes with a collection larger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithCollectionBiggerThanPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new DummyChromosome(3));
        newChromosomes.add(new DummyChromosome(4));
        population.addChromosomes(newChromosomes);
    }

    /**
     * Test the method getFittestChromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest = population.getFittestChromosome();
        assertTrue(initialChromosomes.contains(fittest));
    }

    /**
     * Test the method setPopulationLimit with a valid population limit.
     */
    @Test
    public void testSetPopulationLimitWithValidPopulationLimit() {
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test the method setPopulationLimit with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        population.setPopulationLimit(-1);
    }

    /**
     * Test the method setPopulationLimit with a population limit smaller than the current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithPopulationSizeBiggerThanNewLimit() {
        population.setPopulationLimit(2);
    }

    /**
     * Test the method getPopulationSize.
     */
    @Test
    public void testGetPopulationSize() {
        assertEquals(5, population.getPopulationSize());
    }

    /**
     * Test the method getChromosomes.
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> obtainedChromosomes = population.getChromosomes();
        List<Chromosome> expectedChromosomes = new ArrayList<>(initialChromosomes);
        assertEquals(expectedChromosomes, obtainedChromosomes);
    }

    /**
     * Test the method getChromosomeList.
     */
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> obtainedChromosomes = population.getChromosomeList();
        List<Chromosome> expectedChromosomes = new ArrayList<>(initialChromosomes);
        assertEquals(expectedChromosomes, obtainedChromosomes);
    }
}

/**
 * Dummy chromosome for testing purposes.
 */
class DummyChromosome implements Chromosome {

    private int value;

    public DummyChromosome(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Integer.compare(this.value, ((DummyChromosome) o).value);
    }
}