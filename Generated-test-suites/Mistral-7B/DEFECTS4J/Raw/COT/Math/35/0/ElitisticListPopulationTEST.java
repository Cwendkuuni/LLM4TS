package org.apache.commons.math3.genetics;

import static org.apache.commons.math3.util.ArrayUtils.toString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FitnessEvaluator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;
    private FitnessEvaluator evaluator;

    @Before
    public void setup() {
        evaluator = mock(FitnessEvaluator.class);
        chromosomes = new ArrayList<>();
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals("ElitisticListPopulation[chromosomes=[] size=5 elitismRate=0.5]", population.toString());
    }

    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals("ElitisticListPopulation[chromosomes=[] size=5 elitismRate=0.5]", population.toString());
    }

    @Test
    public void testNextGeneration() {
        chromosomes.add(mockChromosome(1.0));
        chromosomes.add(mockChromosome(0.8));
        chromosomes.add(mockChromosome(0.5));
        chromosomes.add(mockChromosome(0.3));
        chromosomes.add(mockChromosome(0.1));
        chromosomes.add(mockChromosome(0.0));
        Collections.sort(chromosomes);

        population.nextGeneration();

        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.getNextGeneration();
        assertEquals("ElitisticListPopulation[chromosomes=[Chromosome{fitness=0.1}, Chromosome{fitness=0.3}, "
                + "Chromosome{fitness=0.5}, Chromosome{fitness=0.8}, Chromosome{fitness=1.0}] size=5 elitismRate=0.5]",
                nextGeneration.toString());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.6);
        assertEquals("ElitisticListPopulation[chromosomes=[] size=5 elitismRate=0.6]", population.toString());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.1);
    }

    @Test
    public void testGetElitismRate() {
        population.setElitismRate(0.7);
        assertEquals("ElitisticListPopulation[chromosomes=[] size=5 elitismRate=0.7]", population.toString());
        assertEquals(0.7, population.getElitismRate(), 0.0);
    }

    private Chromosome mockChromosome(double fitness) {
        Chromosome chromosome = mock(Chromosome.class);
        when(chromosome.getFitness()).thenReturn(fitness);
        return chromosome;
    }

}