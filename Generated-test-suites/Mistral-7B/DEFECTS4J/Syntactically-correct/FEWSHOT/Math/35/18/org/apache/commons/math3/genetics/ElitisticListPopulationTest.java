package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = createChromosomes();

        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertTrue(population instanceof ElitisticListPopulation);
    }

    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertTrue(population instanceof ElitisticListPopulation);
    }

    @Test
    public void testNextGenerationWithElitism() {
        List<Chromosome> initialChromosomes = createChromosomes();

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 15, 0.5);
        populateChromosomes(population);

        Collections.sort(population);
        int boundIndex = (int) FastMath.ceil((1.0 - population.getElitismRate()) * population.size());
        List<Chromosome> nextGeneration = population.nextGeneration();

        assertEquals(population.size(), population.size() + (population.size() - boundIndex));
        assertEquals(population.getElitismRate(), nextGeneration.getElitismRate(), 0.001);

        for (int i = boundIndex; i < population.size(); i++) {
            Chromosome expectedChromosome = population.get(i);
            Chromosome actualChromosome = nextGeneration.get(i - boundIndex);
            assertEquals(expectedChromosome, actualChromosome);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.setElitismRate(-0.2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        population.setElitismRate(1.2);
    }

    private List<Chromosome> createChromosomes() {
        // Replace this method with actual chromosome creation logic
        return Collections.emptyList();
    }

    private void populateChromosomes(List<? super Chromosome> population) {
        // Replace this method with actual chromosome population logic
    }
}