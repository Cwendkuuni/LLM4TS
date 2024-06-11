import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();
        assertEquals(3, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 1e-6);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(1.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.9, population.getElitismRate(), 1e-6);
    }
}

class Chromosome {} //dummy class