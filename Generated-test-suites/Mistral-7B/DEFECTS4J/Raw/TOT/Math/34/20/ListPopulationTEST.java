package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Tests for {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Tests the constructor with a valid population limit.
     */
    @Test
    public void testValidConstruction() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertNotNull(population.getChromosomes());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests the constructor with an invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstruction() {
        new ListPopulation(-1);
    }

    /**
     * Tests the constructor with an initial list of chromosomes.
     */
    @Test
    public void testInitialList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));

        ListPopulation population = new ListPopulation(10, chromosomes);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertSame(chromosomes, population.getChromosomes());
    }

    /**
     * Tests the constructor with an initial list of chromosomes and an invalid population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInitialListAndInvalidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));
        chromosomes.add(new Chromosome(3.0));

        new ListPopulation(1, chromosomes);
    }

    /**
     * Tests the {@code setChromosomes} method.
     */
    @Test
    public void testSetChromosomes() {
        List<Chromosome> population = new ArrayList<>();
        population.add(new Chromosome(1.0));
        population.add(new Chromosome(2.0));

        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setChromosomes(population);

        Assert.assertEquals(population, listPopulation.getChromosomes());
        Assert.assertEquals(2, listPopulation.getPopulationSize());
    }

    /**
     * Tests the {@code addChromosomes} method.
     */
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new Chromosome(1.0)));
        population.addChromosomes(Collections.singletonList(new Chromosome(2.0)));

        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertSame(population.getChromosomes(), population.getChromosomes());

        ListPopulation expectedPopulation = new ListPopulation(10);
        expectedPopulation.addChromosomes(Collections.singletonList(new Chromosome(1.0)));
        expectedPopulation.addChromosomes(Collections.singletonList(new Chromosome(2.0)));
        expectedPopulation.addChromosomes(Collections.singletonList(new Chromosome(3.0)));

        population.addChromosomes(Collections.singletonList(new Chromosome(3.0)));

        Assert.assertEquals(3, population.getPopulationSize());
        Assert.assertSame(expectedPopulation.getChromosomes(), population.getChromosomes());
    }

    /**
     * Tests the {@code getFittestChromosome} method.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        population.addChromosome(new Chromosome(1.0));
        population.addChromosome(new Chromosome(2.0));
        population.addChromosome(new Chromosome(3.0));

        Chromosome fittest = population.getFittestChromosome();
        Assert.assertNotNull(fittest);

        // The order of chromosomes in the list doesn't matter
        Assert.assertEquals(1.0, fittest.getValue(), 0.001);
    }
}