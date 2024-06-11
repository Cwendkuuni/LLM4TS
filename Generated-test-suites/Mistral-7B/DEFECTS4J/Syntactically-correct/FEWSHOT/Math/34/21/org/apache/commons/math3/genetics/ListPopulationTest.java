package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.impl.DefaultChromosome;

public class ListPopulationTest {

    /**
     * Tests constructor ListPopulation(int populationLimit).
     */
    @Test
    public void testListPopulationInt() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests constructor ListPopulation(Collection<Chromosome> chromosomes, int populationLimit).
     */
    @Test
    public void testListPopulationCollectionInt() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DefaultChromosome(new byte[]{}, 1)), 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());

        // test setChromosomes() deprecated method
        population.setChromosomes(Arrays.asList(new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)));
        assertEquals(5, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());

        // test addChromosomes() method
        population.addChromosomes(Arrays.asList(new DefaultChromosome(new byte[]{}, 4), new DefaultChromosome(new byte[]{}, 5)));
        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
    }

    /**
     * Tests getChromosomes() method.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(10, chromosomes.size());
        assertEquals(new DefaultChromosome(new byte[]{}, 1), chromosomes.get(0));
        assertEquals(new DefaultChromosome(new byte[]{}, 2), chromosomes.get(1));
        assertEquals(new DefaultChromosome(new byte[]{}, 3), chromosomes.get(2));
    }

    /**
     * Tests getChromosomeList() method.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(10, chromosomes.size());
        assertEquals(new DefaultChromosome(new byte[]{}, 1), chromosomes.get(0));
        assertEquals(new DefaultChromosome(new byte[]{}, 2), chromosomes.get(1));
        assertEquals(new DefaultChromosome(new byte[]{}, 3), chromosomes.get(2));
    }

    /**
     * Tests addChromosome() method.
     */
    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2)), 5);
        population.addChromosome(new DefaultChromosome(new byte[]{}, 3));
        assertEquals(5, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
        assertEquals(new DefaultChromosome(new byte[]{}, 3), population.getChromosomes().get(2));
    }

    /**
     * Tests getFittestChromosome() method.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 3), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 1)), 3);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(new DefaultChromosome(new byte[]{}, 3), fittestChromosome);
    }

    /**
     * Tests setPopulationLimit() method.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
        assertFalse(population.getChromosomes().subList(3, population.getChromosomes().size()).isEmpty());
    }

    /**
     * Tests constructor with invalid arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullCollection() {
        new ListPopulation(null, 5);
    }

    /**
     * Tests constructor with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), -1);
    }

    /**
     * Tests constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3), new DefaultChromosome(new byte[]{}, 4)), 5);
    }

    /**
     * Tests constructor with invalid arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        new ListPopulation(null, 5);
    }

    /**
     * Tests constructor with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationSize() {
        new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), -1);
    }

    /**
     * Tests setPopulationLimit() method with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 5);
        population.setPopulationLimit(-1);
    }

    /**
     * Tests setPopulationLimit() method with smaller population size than current number of chromosomes.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 5);
        population.setPopulationLimit(3);
    }

    /**
     * Tests addChromosomes() method with invalid arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 5);
        population.addChromosomes(null);
    }

    /**
     * Tests addChromosomes() method with invalid arguments.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(Arrays.asList(new DefaultChromosome(new byte[]{}, 1), new DefaultChromosome(new byte[]{}, 2), new DefaultChromosome(new byte[]{}, 3)), 5);
        population.addChromosomes(Arrays.asList(new DefaultChromosome(new byte[]{}, 4), new DefaultChromosome(new byte[]{}, 5), new DefaultChromosome(new byte[]{}, 6)));
    }
}