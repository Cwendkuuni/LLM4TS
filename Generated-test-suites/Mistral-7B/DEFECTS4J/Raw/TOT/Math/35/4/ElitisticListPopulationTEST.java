package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;
    private int populationLimit;
    private double elitismRate;

    public ElitisticListPopulationTest(List<Chromosome> chromosomes, int populationLimit, double elitismRate) {
        this.chromosomes = chromosomes;
        this.populationLimit = populationLimit;
        this.elitismRate = elitismRate;
    }

    @Before
    public void setUp() {
        population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, pop.size());
        assertEquals(elitismRate, pop.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        ElitisticListPopulation pop = new ElitisticListPopulation(populationLimit, elitismRate);
        assertEquals(populationLimit, pop.size());
        assertEquals(elitismRate, pop.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation emptyPopulation = new ElitisticListPopulation(0, elitismRate);
        assertThrows(IllegalArgumentException.class, () -> emptyPopulation.nextGeneration());
    }

    @Test
    public void testNextGenerationWithInitialPopulation() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        int oldSize = population.size();
        int newSize = nextGeneration.size();
        int copiedChromosomes = (int) FastMath.ceil((1.0 - elitismRate) * oldSize);
        int elitistChromosomes = (int) FastMath.ceil(elitismRate * oldSize);
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);
        assertEquals(oldSize + populationLimit - elitistChromosomes, nextGeneration.size());
        for (int i = 0; i < elitistChromosomes; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome copiedChromosome = nextGeneration.getChromosome(i);
            assertEquals(chromosome, copiedChromosome);
        }
        for (int i = elitistChromosomes; i < oldSize; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome copiedChromosome = nextGeneration.getChromosome(i + oldSize - elitistChromosomes);
            assertNotEquals(chromosome, copiedChromosome);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.1);
    }

    @Test
    public void testSetElitismRateInRange() {
        population.setElitismRate(elitismRate);
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        Chromosome chromosome4 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);
        Object[] data1 = {chromosomes, 10, 0.5};
        Object[] data2 = {chromosomes, 15, 0.8};
        Object[] data3 = {chromosomes, 5, 1.2};
        return List.of(data1, data2, data3);
    }
}