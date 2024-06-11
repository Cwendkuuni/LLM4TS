package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.ListPopulation.NotPopulationSizeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }

    @Test
    public void testListPopulationConstructor() {
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertFalse(population.canAdd(chromosome1));
        assertFalse(population.canAdd(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesTooMany() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertFalse(population.canAdd(chromosome1));
        assertFalse(population.canAdd(chromosome2));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesTooMany() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population = new ListPopulation(5);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
        assertFalse(population.canAdd(chromosome1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeTooMany() {
        Chromosome chromosome1 = new Chromosome(1);
        population.addChromosome(chromosome1);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertSame(chromosome1, chromosomes.get(0));
        assertSame(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertSame(chromosome2, fittestChromosome);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationIteratorRemove() {
        Chromosome chromosome1 = new Chromosome(1);
        population.addChromosome(chromosome1);
        Iterator<Chromosome> iterator = population.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void testListPopulationSize() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NotPopulationSizeException.class)
    public void testListPopulationSizeGreaterThanLimit() {
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        population.addChromosome(new Chromosome(3));
    }

    @Test
    public void testListPopulationSizeLessThanLimit() {
        Chromosome chromosome1 = new Chromosome(1);
        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
    }
}