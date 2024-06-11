package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.FitnessEvaluator;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCredentials() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationCreationWithChromosomes() {
        Chromosome chromosome1 = new ArrayListChromosome(10, 10);
        Chromosome chromosome2 = new ArrayListChromosome(10, 10);

        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(chromosome1); add(chromosome2); }}, 10, 0.5);

        assertEquals(2, population.size());
        assertEquals(chromosome1, population.get(0));
        assertEquals(chromosome2, population.get(1));
    }

    @Test
    public void testElitisticListPopulationCreationWithPopulationLimit() {
        Chromosome chromosome = new ArrayListChromosome(10, 10);

        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        population.addChromosome(chromosome);
        population.addChromosome(chromosome);

        assertEquals(2, population.size());
        assertEquals(chromosome, population.get(0));
        assertEquals(chromosome, population.get(1));
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(0, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithOneChromosome() {
        Chromosome chromosome = new ArrayListChromosome(10, 10);
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(chromosome); }}, 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(1, nextGeneration.size());
        assertEquals(chromosome, nextGeneration.get(0));
    }

    @Test
    public void testNextGenerationWithTwoChromosomes() {
        Chromosome chromosome1 = new ArrayListChromosome(10, 10);
        Chromosome chromosome2 = new ArrayListChromosome(10, 10);
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(chromosome1); add(chromosome2); }}, 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(2, nextGeneration.size());

        Chromosome bestChromosome = population.get(0);
        assertEquals(bestChromosome, nextGeneration.get(0));
        assertEquals(bestChromosome, nextGeneration.get(1));
    }

    @Test
    public void testNextGenerationWithThreeChromosomes() {
        Chromosome chromosome1 = new ArrayListChromosome(10, 10);
        Chromosome chromosome2 = new ArrayListChromosome(10, 10);
        Chromosome chromosome3 = new ArrayListChromosome(10, 10);
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(chromosome1); add(chromosome2); add(chromosome3); }}, 10, 0.5);

        Population nextGeneration = population.nextGeneration();

        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(3, nextGeneration.size());

        Chromosome bestChromosome = population.get(0);
        Chromosome secondBestChromosome = population.get(1);
        assertEquals(bestChromosome, nextGeneration.get(0));
        assertEquals(bestChromosome, nextGeneration.get(1));
        assertEquals(secondBestChromosome, nextGeneration.get(2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(new ArrayListChromosome(10, 10)); }}, 10, 1.1);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(new ArrayListChromosome(10, 10)); }}, 10, 0.5);
        population.setElitismRate(0.2);
        assertEquals(0.2, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(new ArrayListChromosome(10, 10)); }}, 10, 0.5);
        population.setElitismRate(-0.1);
    }

    @Test
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(new ArrayListChromosome(10, 10)); }}, 10, 0.5);
        population.setElitismRate(1.1);
        assertEquals(1.1, population.getElitismRate(), 0.01);
    }

    @Test
    public void testSortChromosomesAndDetermineElite() {
        Chromosome chromosome1 = new ArrayListChromosome(10, 10);
        Chromosome chromosome2 = new ArrayListChromosome(10, 10);
        Chromosome chromosome3 = new ArrayListChromosome(10, 10);
        ElitisticListPopulation population = new ElitisticListPopulation(
                new ArrayList<Chromosome>() {{ add(chromosome1); add(chromosome2); add(chromosome3); }}, 10, 0.6);

        List<Chromosome> sortedChromosomes = new ArrayList<Chromosome>(population);
        NaturalRanking ranking = new NaturalRanking();
        ranking.rank(sortedChromosomes);

        int limit = (int) FastMath.ceil(sortedChromosomes.size() * 0.6);
        List<Chromosome> eliteChromosomes = new ArrayList<Chromosome>();

        for (int i = 0; i < limit; i++) {
            eliteChromosomes.add(sortedChromosomes.get(i));
        }

        Population nextGeneration = new ElitisticListPopulation(10, 0.6);
        for (Chromosome chromosome : eliteChromosomes) {
            nextGeneration.addChromosome(chromosome);
        }

        assertEquals(eliteChromosomes, nextGeneration.getChromosomes());
    }
}