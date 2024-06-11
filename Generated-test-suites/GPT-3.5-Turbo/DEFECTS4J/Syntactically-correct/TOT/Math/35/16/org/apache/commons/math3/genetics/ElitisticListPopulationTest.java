import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ElitisticListPopulation(chromosomes, 5, 0.9);
    }

    @Test
    public void testConstructorWithChromosomes() {
        assertEquals(3, population.getChromosomes().size());
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        population = new ElitisticListPopulation(5, 0.9);
        assertEquals(0, population.getChromosomes().size());
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(1.5);
    }

    @Test
    public void testSetElitismRateValid() {
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.001);
    }
}