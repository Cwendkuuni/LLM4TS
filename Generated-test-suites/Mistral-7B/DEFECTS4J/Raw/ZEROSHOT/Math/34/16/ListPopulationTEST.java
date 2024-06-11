package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.ListPopulation.Fitness;
import org.junit.Test;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test ListPopulation constructor with positive population limit.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationPositivePopulationLimit() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        assertSame(chromosomes, population.getChromosomes());
    }

    /**
     * Test ListPopulation constructor with null list of chromosomes.
     * @throws Exception if an error occurs
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() throws Exception {
        new ListPopulation(null, 5);
    }

    /**
     * Test ListPopulation constructor with negative population limit.
     * @throws Exception if an error occurs
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(chromosomes, -1);
    }

    /**
     * Test ListPopulation constructor with list of chromosomes bigger than population limit.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationBiggerThanPopulationLimit() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Fitness(3, new Object[]{3.0f, 3.0f}));
        ListPopulation population = new ListPopulation(chromosomes, 2);
    }

    /**
     * Test ListPopulation setChromosomes method with null list of chromosomes.
     * @throws Exception if an error occurs
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesNullChromosomes() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(null);
    }

    /**
     * Test ListPopulation setChromosomes method with list of chromosomes bigger than population limit.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanPopulationLimit() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 2);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        Chromosome chromosome3 = new Fitness(3, new Object[]{3.0f, 3.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.setChromosomes(chromosomes);
    }

    /**
     * Test ListPopulation addChromosomes method with null collection of chromosomes.
     * @throws Exception if an error occurs
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesNullCollection() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    /**
     * Test ListPopulation addChromosomes method with collection of chromosomes bigger than population limit.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 2);
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        Chromosome chromosome3 = new Fitness(3, new Object[]{3.0f, 3.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.addChromosomes(chromosomes);
    }

    /**
     * Test ListPopulation getFittestChromosome method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationGetFittestChromosome() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        Chromosome chromosome3 = new Fitness(3, new Object[]{3.0f, 3.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Chromosome expectedChromosome = chromosomes.get(0);
        Chromosome actualChromosome = population.getFittestChromosome();
        assertSame(expectedChromosome, actualChromosome);
    }

    /**
     * Test ListPopulation getPopulationLimit method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationGetPopulationLimit() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test ListPopulation setPopulationLimit method with negative population limit.
     * @throws Exception if an error occurs
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegativePopulationLimit() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test ListPopulation setPopulationLimit method with population limit smaller than current population size.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerCurrentPopulationSize() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        population.setPopulationLimit(3);
    }

    /**
     * Test ListPopulation getPopulationSize method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationGetPopulationSize() throws Exception {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test ListPopulation toString method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationToString() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        String expectedString = "[Chromosome[fitness=1.0, genotype=[1.0, 1.0]], Chromosome[fitness=2.0, genotype=[2.0, 2.0]]]";
        String actualString = population.toString();
        assertEquals(expectedString, actualString);
    }

    /**
     * Test ListPopulation iterator method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testListPopulationIterator() throws Exception {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Fitness(1, new Object[]{1.0f, 1.0f});
        Chromosome chromosome2 = new Fitness(2, new Object[]{2.0f, 2.0f});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome expectedChromosome1 = chromosomes.get(0);
        Chromosome expectedChromosome2 = chromosomes.get(1);
        Chromosome actualChromosome1 = iterator.next();
        Chromosome actualChromosome2 = iterator.next();
        assertSame(expectedChromosome1, actualChromosome1);
        assertSame(expectedChromosome2, actualChromosome2);
        assertFalse(iterator.hasNext());
    }
}