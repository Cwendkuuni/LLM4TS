package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationCreation() {
        ListPopulation population = new ListPopulation(5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());

        ListPopulation populationWithChromosomes = new ListPopulation(Collections.singletonList(new Chromosome() {}), 5);
        assertEquals(1, populationWithChromosomes.getPopulationSize());
        assertSame(populationWithChromosomes.getChromosomes().get(0), populationWithChromosomes.getFittestChromosome());
    }

    @Test
    public void testListPopulationCreationWithNullList() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, 5));
    }

    @Test
    public void testListPopulationCreationWithBigList() {
        List<Chromosome> bigList = new ArrayList<>(6);
        for (int i = 0; i < 11; i++) {
            bigList.add(new Chromosome() {});
        }
        assertThrows(NumberIsTooLargeException.class,
                () -> new ListPopulation(bigList, 5));
    }

    @Test
    public void testListPopulationCreationWithPopulationLimitLessThanZero() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(-1));
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes.size(), population.getPopulationSize());
        for (Chromosome chromosome : population.getChromosomes()) {
            assertTrue(chromosomes.contains(chromosome));
        }
    }

    @Test
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(null));
    }

    @Test
    public void testSetChromosomesWithBigList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> bigList = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            bigList.add(new Chromosome() {});
        }
        assertThrows(NumberIsTooLargeException.class, () -> population.setChromosomes(bigList));
    }

    @Test
    public void testSetChromosomesWithPopulationLimitLessThanSize() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {});
        assertThrows(NumberIsTooSmallException.class, () -> population.setChromosomes(chromosomes));
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes.size(), population.getPopulationSize());
        for (Chromosome chromosome : population.getChromosomes()) {
            assertTrue(chromosomes.contains(chromosome));
        }
    }

    @Test
    public void testAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> population.addChromosomes(null));
    }

    @Test
    public void testAddChromosomesWithBigCollection() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> bigList = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            bigList.add(new Chromosome() {});
        }
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(bigList));
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        population.addChromosome(chromosome);
        assertEquals(6, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test
    public void testAddChromosomeWithPopulationLimitLessThanOne() {
        ListPopulation population = new ListPopulation(0);
        Chromosome chromosome = new Chromosome() {};
        assertThrows(NotPositiveException.class, () -> population.addChromosome(chromosome));
    }

    @Test
    public void testAddChromosomeWithPopulationLimitFull() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(chromosome));
    }
}