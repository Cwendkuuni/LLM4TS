package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {});

        new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        Chromosome chromosome3 = new Chromosome() {};
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        Chromosome chromosome3 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertSame(chromosome1, chromosomes.get(0));
        assertSame(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(2, chromosomes.size());
        assertSame(chromosome1, chromosomes.get(0));
        assertSame(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithFullPopulation() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(new Chromosome() {});
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        Chromosome bestChromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(bestChromosome);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertSame(bestChromosome, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithTooSmallPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        String expected = "[" + chromosome1.toString() + ", " + chromosome2.toString() + "]";
        assertEquals(expected, population.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        List<Chromosome> expectedList = Arrays.asList(chromosome1, chromosome2);
        List<Chromosome> list = new ArrayList<>();
        for (Chromosome chromosome : population) {
            list.add(chromosome);
        }
        assertEquals(expectedList, list);
    }
}