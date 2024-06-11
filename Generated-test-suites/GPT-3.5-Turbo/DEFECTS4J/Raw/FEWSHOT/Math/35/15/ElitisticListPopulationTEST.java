import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ElitisticListPopulation(chromosomes, 5, 0.5);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(2, nextGeneration.getChromosomes().size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRate() {
        population.setElitismRate(-0.1);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    private class Chromosome implements Comparable<Chromosome> {
        // Test implementation of Chromosome class
        @Override
        public int compareTo(Chromosome other) {
            return 0;
        }
    }
}