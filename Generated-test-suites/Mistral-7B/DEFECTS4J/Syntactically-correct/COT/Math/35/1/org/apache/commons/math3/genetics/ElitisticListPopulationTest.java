package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);

        assertEquals(5, population.size());
        assertEquals(chromosome1, population.get(0));
        assertEquals(chromosome2, population.get(1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 1.1);
    }

    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRateLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(-1, 0.9);
    }

    @Test
    public void testElitisticListPopulationGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 5, 0.9);
        population.setElitismRate(1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);

        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 2; i++) {
            assertTrue(nextGeneration.contains(chromosomes.get(i)));
        }
        for (int i = 2; i < 5; i++) {
            assertFalse(nextGeneration.contains(chromosomes.get(i)));
        }
    }
}

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return 1.0;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Object getGene(int index) {
        return null;
    }

    @Override
    public void setGene(int index, Object gene) {

    }

    @Override
    public String toString() {
        return "DummyChromosome: " + id;
    }
}