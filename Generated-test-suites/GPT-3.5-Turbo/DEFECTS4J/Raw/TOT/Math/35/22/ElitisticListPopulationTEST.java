import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private List<Chromosome> chromosomes;
    private final int populationLimit = 100;
    private final double elitismRate = 0.9;
    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testConstructorWithChromosomes() {
        assertEquals(chromosomes, elitisticListPopulation.getChromosomes());
        assertEquals(populationLimit, elitisticListPopulation.getPopulationLimit());
        assertEquals(elitismRate, elitisticListPopulation.getElitismRate(), 0.00001);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        elitisticListPopulation = new ElitisticListPopulation(populationLimit, elitismRate);
        assertNotNull(elitisticListPopulation.getChromosomes());
        assertEquals(populationLimit, elitisticListPopulation.getPopulationLimit());
        assertEquals(elitismRate, elitisticListPopulation.getElitismRate(), 0.00001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowLowerBound() {
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveUpperBound() {
        elitisticListPopulation.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRateWithinBounds() {
        elitisticListPopulation.setElitismRate(0.2);
        assertEquals(0.2, elitisticListPopulation.getElitismRate(), 0.00001);
    }

    @Test
    public void testNextGeneration() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().contains(chromosome1));
        assertFalse(nextGeneration.getChromosomes().contains(chromosome2));
        assertFalse(nextGeneration.getChromosomes().contains(chromosome3));
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(elitismRate, elitisticListPopulation.getElitismRate(), 0.00001);
    }
}