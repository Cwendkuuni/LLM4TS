package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        Chromosome chromosome1 = new FooChromosome(1);
        Chromosome chromosome2 = new FooChromosome(2);
        Chromosome chromosome3 = new FooChromosome(3);
        Chromosome chromosome4 = new FooChromosome(4);
        Chromosome chromosome5 = new FooChromosome(5);
        List<Chromosome> chromosomes = List.of(chromosome1, chromosome2, chromosome3, chromosome4, chromosome5);
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testElitisticListPopulationConstructorWithList() {
        ElitisticListPopulation population = new ElitisticListPopulation(population.getChromosomes(), population.getPopulationLimit(), population.getElitismRate());
        assertEquals(population.getChromosomes(), population.getChromosomes());
        assertEquals(population.getPopulationLimit(), population.getPopulationLimit());
        assertEquals(population.getElitismRate(), population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(population.getPopulationLimit(), population.getElitismRate());
        assertEquals(population.getChromosomes(), population.getChromosomes());
        assertEquals(population.getPopulationLimit(), population.getPopulationLimit());
        assertEquals(population.getElitismRate(), population.getElitismRate());
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertEquals(population.getPopulationLimit(), nextGeneration.size());

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome chromosome = oldChromosomes.get(i);
            boolean foundInNextGeneration = false;
            for (Chromosome nextChromosome : nextGeneration.getChromosomes()) {
                if (chromosome.equals(nextChromosome)) {
                    foundInNextGeneration = true;
                    break;
                }
            }
            assertTrue(foundInNextGeneration);
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(population.getPopulationLimit(), population.getElitismRate());

        double newElitismRate = 0.6;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutsideRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(population.getPopulationLimit(), population.getElitismRate());

        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);
    }

}

class FooChromosome implements Chromosome {
    private int value;

    public FooChromosome(int value) {
        this.value = value;
    }

    @Override
    public int getFitness() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FooChromosome)) {
            return false;
        }
        FooChromosome other = (FooChromosome) obj;
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return "FooChromosome{" + "value=" + value + '}';
    }
}