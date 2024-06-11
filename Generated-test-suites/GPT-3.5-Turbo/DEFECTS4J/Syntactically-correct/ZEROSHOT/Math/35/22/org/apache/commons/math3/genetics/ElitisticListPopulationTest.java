import static org.junit.Assert.*;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(chromosomes, elitisticListPopulation.getChromosomes());
    }

    @Test
    public void testConstructorWithPopulationLimit() {
        elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, elitisticListPopulation.getPopulationLimit());
    }

    @Test
    public void testConstructorWithElitismRate() {
        elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        assertEquals(0.9, elitisticListPopulation.getElitismRate(), 0);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        assertEquals(9, nextGenerationChromosomes.size());
        for (int i = 1; i < 10; i++) {
            assertEquals(i, nextGenerationChromosomes.get(i-1).getValue());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowRange() {
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveRange() {
        elitisticListPopulation.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRateInRange() {
        elitisticListPopulation.setElitismRate(0.5);
        assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.9, elitisticListPopulation.getElitismRate(), 0);
    }
}