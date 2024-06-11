import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private static final int POPULATION_LIMIT = 10;
    private static final double ELITISM_RATE = 0.9;

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        elitisticListPopulation = new ElitisticListPopulation(POPULATION_LIMIT, ELITISM_RATE);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // Create some chromosomes
        for(int i = 0; i < POPULATION_LIMIT; i++) {
            chromosomes.add(new Chromosome(i));
        }
        elitisticListPopulation.setChromosomes(chromosomes);

        // Generate next generation
        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();
        
        // Check if the size of the next generation is correct
        assertEquals(POPULATION_LIMIT, nextGeneration.getChromosomes().size());

        // Check if the chromosomes copied from the previous generation are correct
        for(int i = 0; i < POPULATION_LIMIT; i++) {
            assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(i)));
        }
    }

    @Test
    public void testSetElitismRate() {
        double newElitismRate = 0.5;
        elitisticListPopulation.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, elitisticListPopulation.getElitismRate(), 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        double newElitismRate = -0.1;
        elitisticListPopulation.setElitismRate(newElitismRate);
    }
}