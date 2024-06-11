import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElitisticListPopulationTest {

    private static final int POPULATION_LIMIT = 10;
    private static final double ELITISM_RATE = 0.9;

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        elitisticListPopulation = new ElitisticListPopulation(POPULATION_LIMIT, ELITISM_RATE);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome(new Random()));
        }

        elitisticListPopulation = new ElitisticListPopulation(chromosomes, POPULATION_LIMIT, ELITISM_RATE);
        assertEquals(chromosomes, elitisticListPopulation.getChromosomes());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            chromosomes.add(new Chromosome(new Random()));
        }

        elitisticListPopulation = new ElitisticListPopulation(chromosomes, POPULATION_LIMIT, ELITISM_RATE);
        Population nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(POPULATION_LIMIT, nextGeneration.getPopulationLimit());

        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        for (Chromosome chromosome : chromosomes) {
            assertTrue(nextGenerationChromosomes.contains(chromosome));
        }
    }

    @Test
    public void testSetElitismRateValid() {
        elitisticListPopulation.setElitismRate(0.5);
        assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test
    public void testSetElitismRateInvalid() {
        try {
            elitisticListPopulation.setElitismRate(1.5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertTrue(true);
        }
    }

    @After
    public void tearDown() {
        elitisticListPopulation = null;
    }
}