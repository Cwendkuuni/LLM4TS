
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0);

        population = new ElitisticListPopulation(100, 0.5);
        nextGeneration = population.nextGeneration();
        assertEquals(100, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0);

        population = new ElitisticListPopulation(100, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0);
    }
}
