
package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation1() {
        // create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // create an elitistic list population with population limit 3 and elitism rate 0.9
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.9);

        // check the population limit
        assertEquals(3, population.getPopulationLimit());

        // check the elitism rate
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test
    public void testElitisticListPopulation2() {
        // create an elitistic list population with population limit 5 and elitism rate 0.7
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.7);

        // check the population limit
        assertEquals(5, population.getPopulationLimit());

        // check the elitism rate
        assertEquals(0.7, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        // create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // create an elitistic list population with population limit 5 and elitism rate 0.6
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.6);

        // get the next generation
        Population nextGeneration = population.nextGeneration();

        // check the size of the next generation
        assertEquals(3, nextGeneration.getPopulationSize());
    }

    @Test
    public void testSetElitismRate() {
        // create an elitistic list population with population limit 5 and elitism rate 0.6
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.6);

        // set a new elitism rate of 0.8
        population.setElitismRate(0.8);

        // check the elitism rate
        assertEquals(0.8, population.getElitismRate(), 0.0);
    }
}
