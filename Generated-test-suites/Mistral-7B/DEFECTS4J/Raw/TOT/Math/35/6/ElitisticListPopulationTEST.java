package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        Chromosome chromosome1 = new FooChromosome(1);
        Chromosome chromosome2 = new FooChromosome(2);
        Chromosome chromosome3 = new FooChromosome(3);
        Chromosome chromosome4 = new FooChromosome(4);
        Chromosome chromosome5 = new FooChromosome(5);

        List<Chromosome> chromosomes = List.of(chromosome1, chromosome2, chromosome3, chromosome4, chromosome5);

        this.population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        assertNotNull(this.population);
        assertEquals(10, this.population.getPopulationLimit());
        assertEquals(5.0, this.population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test
    public void testConstructorInvalidElitismRate() {
        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(this.population, 10, 1.1));
    }

    /**
     * Test method {@link ElitisticListPopulation#nextGeneration()}.
     */
    @Test
    public void testNextGeneration() {
        Population nextGeneration = this.population.nextGeneration();

        assertEquals(10, nextGeneration.getSize());
        assertTrue(nextGeneration instanceof ElitisticListPopulation);

        Chromosome bestChromosome = this.population.getChromosome(0);
        Chromosome newBestChromosome = ((ElitisticListPopulation) nextGeneration).getChromosome(0);

        assertEquals(bestChromosome, newBestChromosome);

        for (int i = 1; i < this.population.getSize(); i++) {
            Chromosome chromosome = this.population.getChromosome(i);
            assertFalse(nextGeneration.contains(chromosome));
        }
    }

    /**
     * Test method {@link ElitisticListPopulation#setElitismRate(double)}.
     */
    @Test
    public void testSetElitismRate() {
        this.population.setElitismRate(0.7);
        assertEquals(0.7, this.population.getElitismRate(), 0.01);
    }

    /**
     * Test method {@link ElitisticListPopulation#setElitismRate(double)} with invalid elitism rate.
     */
    @Test
    public void testSetElitismRateInvalidElitismRate() {
        assertThrows(OutOfRangeException.class, () -> this.population.setElitismRate(-0.1));
        assertThrows(OutOfRangeException.class, () -> this.population.setElitismRate(1.1));
    }

}

class FooChromosome implements Chromosome {

    private int value;

    public FooChromosome(int value) {
        this.value = value;
    }

    @Override
    public int getFitness() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}