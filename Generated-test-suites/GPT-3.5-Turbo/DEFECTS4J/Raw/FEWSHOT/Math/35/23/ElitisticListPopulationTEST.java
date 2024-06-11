
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulation_OutOfRangeException() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            int populationLimit = 100;
            double elitismRate = 1.5;
            ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        } catch (OutOfRangeException e) {
            assertEquals("Elitism rate (1.5) is not in the range [0, 1]", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testNextGeneration() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome() {
                @Override
                public double fitness() {
                    return 10;
                }
            });
            chromosomes.add(new Chromosome() {
                @Override
                public double fitness() {
                    return 5;
                }
            });
            chromosomes.add(new Chromosome() {
                @Override
                public double fitness() {
                    return 2;
                }
            });

            int populationLimit = 2;
            double elitismRate = 0.5;

            ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
            Population nextGeneration = population.nextGeneration();

            assertEquals(2, nextGeneration.getChromosomes().size());
            assertEquals(10.0, nextGeneration.getChromosomes().get(0).fitness(), 0.0);
            assertEquals(5.0, nextGeneration.getChromosomes().get(1).fitness(), 0.0);
        } catch (OutOfRangeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetElitismRate() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            int populationLimit = 100;
            double elitismRate = 0.8;

            ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, 0.0);
            population.setElitismRate(elitismRate);

            assertEquals(elitismRate, population.getElitismRate(), 0.0);
        } catch (OutOfRangeException e) {
            e.printStackTrace();
        }
    }

}
