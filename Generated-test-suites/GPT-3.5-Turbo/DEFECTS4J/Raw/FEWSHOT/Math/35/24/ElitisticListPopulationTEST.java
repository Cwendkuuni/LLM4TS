
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(4, nextGeneration.getChromosomes().size());
        
        // TODO: Add more assertions
        
    }
}
