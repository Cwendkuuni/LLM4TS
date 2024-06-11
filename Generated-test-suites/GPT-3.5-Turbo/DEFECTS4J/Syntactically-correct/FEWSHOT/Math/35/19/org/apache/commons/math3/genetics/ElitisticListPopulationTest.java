package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation_OneParameterConstructor() {
        assertThrows(NullPointerException.class, () -> new ElitisticListPopulation(10));
    }

    @Test
    public void testElitisticListPopulation_TwoParameterConstructor() {
        assertThrows(NullPointerException.class, () -> new ElitisticListPopulation(null, 10));
        assertThrows(NullPointerException.class, () -> new ElitisticListPopulation(Collections.emptyList(), 10));
        assertThrows(IllegalArgumentException.class, () -> new ElitisticListPopulation(Collections.singletonList(null), 10));
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(10);
        assertThrows(NullPointerException.class, () -> population.nextGeneration());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10);
        assertThrows(IllegalArgumentException.class, () -> population.setElitismRate(-0.1));
        assertThrows(IllegalArgumentException.class, () -> population.setElitismRate(1.1));
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10);
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }
}