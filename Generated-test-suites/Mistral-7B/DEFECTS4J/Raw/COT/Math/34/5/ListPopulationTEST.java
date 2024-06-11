package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-5);
    }

    @Test
    public void testListPopulationConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[20]);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[20]);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        Assert.assertEquals(6, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(5));

        population.addChromosomes(Collections.singletonList(chromosome2));
        Assert.assertEquals(7, population.getPopulationSize());
        Assert.assertEquals(chromosome2, population.getChromosomes().get(6));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[16]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));

        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(chromosome1);
        expectedChromosomes.add(chromosome2);

        List<Chromosome> chromosomes = population.getChromosomes();

        Assert.assertEquals(expectedChromosomes, chromosomes);
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));

        List<Chromosome> chromosomes = population.getChromosomeList();

        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);

        population.addChromosome(chromosome1);
        Assert.assertEquals(6, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(5));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[16]);

        population.addChromosome(chromosome1);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);
        Chromosome chromosome3 = new Chromosome(new int[20]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
        population.addChromosome(chromosome3);

        Chromosome expectedFittestChromosome = chromosome3;

        Chromosome fittestChromosome = population.getFittestChromosome();

        Assert.assertEquals(expectedFittestChromosome, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());

        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithCurrentPopulationSizeGreaterThanNewPopulationLimit() {
        ListPopulation population = new ListPopulation(15);
        population.setPopulationLimit(10);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(0, population.getPopulationSize());

        population.addChromosomes(Collections.singletonList(new Chromosome(new int[10])));
        Assert.assertEquals(1, population.getPopulationSize());

        population.addChromosomes(Collections.singletonList(new Chromosome(new int[15])));
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));

        String expectedString = "[Lorg.apache.commons.math3.genetics.Chromosome;@";
        String string = population.toString();

        Assert.assertEquals(expectedString, string);
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new int[10]);
        Chromosome chromosome2 = new Chromosome(new int[15]);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));

        Iterator<Chromosome> iterator = population.iterator();

        Assert.assertEquals(chromosome1, iterator.next());
        Assert.assertEquals(chromosome2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}