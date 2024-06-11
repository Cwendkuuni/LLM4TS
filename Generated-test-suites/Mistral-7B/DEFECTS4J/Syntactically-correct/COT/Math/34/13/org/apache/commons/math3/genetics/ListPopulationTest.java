package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for ListPopulation class.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testListPopulationValidConstructor() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationInvalidConstructor() {
        new ListPopulation(-1);
    }

    /**
     * Test setChromosomes method with valid list.
     */
    @Test
    public void testSetChromosomesValidList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] {new Chromosome(), new Chromosome(), new Chromosome()});
        population.setChromosomes(chromosomes);
        assertEquals(3, population.getPopulationSize());
    }

    /**
     * Test setChromosomes method with invalid list.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesInvalidList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Test setChromosomes method with list larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(3);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] {new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome()});
        population.setChromosomes(chromosomes);
    }

    /**
     * Test addChromosomes method with valid collection.
     */
    @Test
    public void testAddChromosomesValidCollection() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] {new Chromosome(), new Chromosome(), new Chromosome()});
        population.addChromosomes(chromosomes);
        assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with invalid collection.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesInvalidCollection() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Test addChromosomes method with collection larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(3);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[] {new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome()});
        population.addChromosomes(chromosomes);
    }

    /**
     * Test getChromosomes method.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> expected = Arrays.asList(new Chromosome[5]);
        population.addChromosomes(Arrays.asList(new Chromosome[5]));
        List<Chromosome> actual = population.getChromosomes();
        assertEquals(expected, actual);
    }

    /**
     * Test getChromosomeList method.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> expected = Arrays.asList(new Chromosome[5]);
        population.addChromosomes(Arrays.asList(new Chromosome[5]));
        List<Chromosome> actual = population.getChromosomeList();
        assertEquals(expected, actual);
    }

    /**
     * Test addChromosome method with valid chromosome.
     */
    @Test
    public void testAddChromosomeValidChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test addChromosome method with invalid chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeInvalidChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    /**
     * Test addChromosome method with population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    /**
     * Test getFittestChromosome method.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        Chromosome expected = population.getChromosomes().get(0);
        Chromosome actual = population.getFittestChromosome();
        assertEquals(expected, actual);
    }

    /**
     * Test getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with valid population limit.
     */
    @Test
    public void testSetPopulationLimitValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test setPopulationLimit method with current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(5);
    }

    /**
     * Test getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        assertEquals(3, population.getPopulationSize());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        String expected = "[org.apache.commons.math3.genetics.Chromosome@1b6d758, org.apache.commons.math3.genetics.Chromosome@4d967f62, org.apache.commons.math3.genetics.Chromosome@3b6d7a7d, org.apache.commons.math3.genetics.Chromosome@7856e3f4, org.apache.commons.math3.genetics.Chromosome@16982e6]";
        String actual = population.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome expected = population.getChromosomes().get(0);
        Chromosome actual = iterator.next();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }
}