package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.75);
        Population nextGeneration = population.nextGeneration();

        assertEquals(4, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.75);
        population.setElitismRate(0.5);

        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.75);

        assertThrows(OutOfRangeException.class, () -> {
            population.setElitismRate(1.5);
        });

        assertThrows(OutOfRangeException.class, () -> {
            population.setElitismRate(-0.5);
        });
    }
}