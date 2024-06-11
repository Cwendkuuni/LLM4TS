package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));
        assertEquals(chromosome2, population.getChromosomeList().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithTooManyChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);

        new ListPopulation(chromosomes, 1);

        ListPopulation population = new ListPopulation(chromosomes, 2);
        assertEquals(2, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());

        throw new AssertionError("Expected NumberIsTooLargeException");
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));
        assertEquals(chromosome2, population.getChromosomeList().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);

        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);

        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(chromosome1);
        newChromosomes.add(new Chromosome(6));

        throw new AssertionError("Expected NumberIsTooLargeException");
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        ListPopulation population = new ListPopulation(5);

        population.addChromosomes(Collections.singletonList(chromosome1));
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));

        population.addChromosomes(Collections.singletonList(chromosome2));
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));
        assertEquals(chromosome2, population.getChromosomeList().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        ListPopulation population = new ListPopulation(5);

        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));

        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(chromosome1);
        newChromosomes.add(new Chromosome(3));

        throw new AssertionError("Expected NumberIsTooLargeException");
    }

    @Test
    public void testGetChromosomes() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(2, actualChromosomes.size());
        assertEquals(chromosome1, actualChromosomes.get(0));
        assertEquals(chromosome2, actualChromosomes.get(1));
    }

    @Test
    public void testGetChromosomeList() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> actualChromosomes = population.getChromosomeList();
        assertEquals(2, actualChromosomes.size());
        assertEquals(chromosome1, actualChromosomes.get(0));
        assertEquals(chromosome2, actualChromosomes.get(1));
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        ListPopulation population = new ListPopulation(5);

        population.addChromosome(chromosome1);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));

        population.addChromosome(chromosome2);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomeList().get(0));
        assertEquals(chromosome2, population.getChromosomeList().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeWithNullChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithFullPopulation() {
        Chromosome chromosome1 = new Chromosome(1);
        ListPopulation population = new ListPopulation(5);

        population.addChromosome(chromosome1);

        throw new AssertionError("Expected NumberIsTooLargeException");
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(5);
        ListPopulation population = new ListPopulation(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Chromosome actualChromosome = population.getFittestChromosome();
        assertEquals(chromosome1, actualChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithTooSmallValue() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));

        population.setPopulationLimit(3);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new Chromosome(1));
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        String actualString = population.toString();
        String expectedString = "[Chromosome 1, Chromosome 2]";
        assertTrue(actualString.equals(expectedString) || actualString.startsWith(expectedString));
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        ListPopulation population = new ListPopulation(Arrays.asList(chromosome1, chromosome2), 5);

        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> actualChromosomes = new ArrayList<>();
        while (iterator.hasNext()) {
            actualChromosomes.add(iterator.next());
        }

        assertEquals(2, actualChromosomes.size());
        assertEquals(chromosome1, actualChromosomes.get(0));
        assertEquals(chromosome2, actualChromosomes.get(1));

        assertFalse(iterator.hasNext());
    }
}