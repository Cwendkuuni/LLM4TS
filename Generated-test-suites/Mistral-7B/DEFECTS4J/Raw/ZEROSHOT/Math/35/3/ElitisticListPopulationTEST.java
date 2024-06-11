package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationInitialization() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationSize());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = -0.1;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 1.2;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        for (int i = 0; i < populationLimit; i++) {
            chromosomes.add(new DummyChromosome(i));
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        assertEquals(populationLimit, nextGeneration.getPopulationSize());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);

        ElitisticListPopulation nextPopulation = (ElitisticListPopulation) nextGeneration;
        List<Chromosome> nextChromosomes = nextPopulation.getChromosomes();

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome nextChromosome = nextChromosomes.get(i - boundIndex);
            assertEquals(oldChromosome, nextChromosome);
        }
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateNegative() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        population.setElitismRate(1.2);
    }
}

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenotype() {
        return new double[0];
    }

    @Override
    public double getFitness() {
        return id + 0.1;
    }
}