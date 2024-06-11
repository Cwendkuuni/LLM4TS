package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test
    public void testConstructor() {
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(-10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        new ListPopulation(0);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new MyChromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MyChromosome());
        chromosomes.add(new MyChromosome());
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new MyChromosome());
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MyChromosome());
        chromosomes.add(new MyChromosome());
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new MyChromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new MyChromosome(10);
        Chromosome chromosome2 = new MyChromosome(5);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testGetFittestChromosomeWithEqualFitness() {
        Chromosome chromosome1 = new MyChromosome(10);
        Chromosome chromosome2 = new MyChromosome(10);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertTrue(population.getFittestChromosome() == chromosome1 || population.getFittestChromosome() == chromosome2);
    }

    @Test
    public void testGetFittestChromosomeWithEmptyPopulation() {
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        population.setPopulationLimit(-10);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroLimit() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerPopulationSize() {
        population.addChromosome(new MyChromosome());
        population.setPopulationLimit(0);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new MyChromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        String expected = "[Chromosome]";
        population.addChromosome(new MyChromosome());
        assertEquals(expected, population.toString());
    }
}

class MyChromosome implements Chromosome {
    private double fitness;

    public MyChromosome() {
        this.fitness = 0;
    }

    public MyChromosome(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int compareTo(Chromosome other) {
        return Double.compare(this.fitness, other.getFitness());
    }

    public void checkValidity() {}
}