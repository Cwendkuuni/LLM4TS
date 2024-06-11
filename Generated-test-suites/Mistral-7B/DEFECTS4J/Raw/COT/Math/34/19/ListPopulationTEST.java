package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test constructor with positive populationLimit.
     */
    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test constructor with negative populationLimit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    /**
     * Test constructor with empty list of chromosomes.
     */
    @Test
    public void testListPopulationConstructorWithEmptyListOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test constructor with non-empty list of chromosomes.
     */
    @Test
    public void testListPopulationConstructorWithNonEmptyListOfChromosomes() {
        Chromosome chromosome = new DummyChromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertNotSame(chromosomes, population.getChromosomes());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test method setChromosomes with null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullListOfChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Test method setChromosomes with list of chromosomes bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListOfChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.setChromosomes(chromosomes);
    }

    /**
     * Test method setChromosomes with list of chromosomes equal to population limit.
     */
    @Test
    public void testSetChromosomesWithListOfChromosomesEqualToPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertNotSame(chromosomes, population.getChromosomes());
        assertEquals(chromosomes.get(0), population.getChromosomes().get(0));
    }

    /**
     * Test method setChromosomes with list of chromosomes smaller than population limit.
     */
    @Test
    public void testSetChromosomesWithListOfChromosomesSmallerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertNotSame(chromosomes, population.getChromosomes());
        assertEquals(chromosomes.get(0), population.getChromosomes().get(0));
    }

    /**
     * Test method addChromosomes with null collection of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullCollectionOfChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Test method addChromosomes with collection of chromosomes bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithCollectionOfChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.addChromosomes(chromosomes);
        chromosomes.add(new DummyChromosome());
    }

    /**
     * Test method addChromosomes with collection of chromosomes equal to population limit.
     */
    @Test
    public void testAddChromosomesWithCollectionOfChromosomesEqualToPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertNotSame(chromosomes, population.getChromosomes());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    /**
     * Test method addChromosomes with collection of chromosomes smaller than population limit.
     */
    @Test
    public void testAddChromosomesWithCollectionOfChromosomesSmallerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertNotSame(chromosomes, population.getChromosomes());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    /**
     * Test method getChromosomes.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.addChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomes();
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertNotSame(chromosomes, result);
        assertTrue(result.containsAll(chromosomes));
    }

    /**
     * Test method getChromosomeList.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome());
        population.addChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, result);
    }

    /**
     * Test method addChromosome with null chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeWithNullChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    /**
     * Test method addChromosome with chromosome bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithChromosomeBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
    }

    /**
     * Test method addChromosome with population limit equal to population size + 1.
     */
    @Test
    public void testAddChromosomeWithPopulationLimitEqualToPopulationSizePlusOne() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.addChromosome(new DummyChromosome());
        assertEquals(5, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
    }

    /**
     * Test method addChromosome with population limit smaller than population size + 1.
     */
    @Test
    public void testAddChromosomeWithPopulationLimitSmallerThanPopulationSizePlusOne() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.addChromosome(new DummyChromosome());
        assertEquals(5, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
        population.setPopulationLimit(4);
        population.addChromosome(new DummyChromosome());
        assertEquals(4, population.getPopulationLimit());
        assertEquals(4, population.getPopulationSize());
    }

    /**
     * Test method getFittestChromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new DummyChromosome());
        }
        population.addChromosomes(chromosomes);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        assertNotNull(fittestChromosome);
    }

    /**
     * Test method getPopulationLimit.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit with negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-5);
    }

    /**
     * Test method setPopulationLimit with population limit smaller than current population size.
     */
    @Test
    public void testSetPopulationLimitWithPopulationLimitSmallerThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.setPopulationLimit(4);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test method setPopulationLimit with population limit equal to current population size.
     */
    @Test
    public void testSetPopulationLimitWithPopulationLimitEqualToCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test method setPopulationLimit with population limit greater than current population size.
     */
    @Test
    public void testSetPopulationLimitWithPopulationLimitGreaterThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.addChromosome(new DummyChromosome());
        population.setPopulationLimit(7);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Test method getPopulationSize.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.addChromosome(new DummyChromosome());
        assertEquals("[DummyChromosome@" + population.hashCode() + ", DummyChromosome@" + (population.hashCode() + 1) + "]", population.toString());
    }

    /**
     * Test method iterator.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome());
        population.addChromosome(new DummyChromosome());
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), population.getChromosomes().get(0));
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), population.getChromosomes().get(1));
        assertFalse(iterator.hasNext());
    }

    private static class DummyChromosome implements Chromosome {
        // empty implementation
    }
}