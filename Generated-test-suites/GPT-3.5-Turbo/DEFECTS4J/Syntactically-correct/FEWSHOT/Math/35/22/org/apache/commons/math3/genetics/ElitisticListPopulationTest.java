package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.0);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testConstructorOutOfRange() {
        try {
            new ElitisticListPopulation(10, -0.1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
            assertEquals(-0.1, e.getValue());
            assertEquals(0, e.getMin());
            assertEquals(1, e.getMax());
        }

        try {
            new ElitisticListPopulation(10, 1.1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
            assertEquals(1.1, e.getValue());
            assertEquals(0, e.getMin());
            assertEquals(1, e.getMax());
        }
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(6));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 4, 0.6);
        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(4, nextGeneration.getPopulationLimit());
        assertEquals(0.6, nextGeneration.getElitismRate(), 0.0);

        List<Chromosome> nextChromosomes = nextGeneration.getChromosomes();
        assertEquals(4, nextChromosomes.size());
        assertTrue(nextChromosomes.contains(new Chromosome(1)));
        assertTrue(nextChromosomes.contains(new Chromosome(2)));
        assertTrue(nextChromosomes.contains(new Chromosome(3)));
        assertFalse(nextChromosomes.contains(new Chromosome(4)));
        assertFalse(nextChromosomes.contains(new Chromosome(5)));
        assertFalse(nextChromosomes.contains(new Chromosome(6)));
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);

        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.0);

        population.setElitismRate(0.0);
        assertEquals(0.0, population.getElitismRate(), 0.0);

        population.setElitismRate(1.0);
        assertEquals(1.0, population.getElitismRate(), 0.0);
    }

}