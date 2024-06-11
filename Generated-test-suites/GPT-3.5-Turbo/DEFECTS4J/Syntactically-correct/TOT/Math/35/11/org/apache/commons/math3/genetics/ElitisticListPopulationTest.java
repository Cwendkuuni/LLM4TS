import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    private static final double DELTA = 1e-15;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 2, 0.8);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOutOfBoundsLow() {
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOutOfBoundsHigh() {
        elitisticListPopulation.setElitismRate(1.1);
    }

    @Test
    public void testElitismRateInBounds() {
        elitisticListPopulation.setElitismRate(0.5);
        assertEquals(0.5, elitisticListPopulation.getElitismRate(), DELTA);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(2, nextGeneration.getPopulationLimit());
        assertEquals(2 * (1 - 0.8), nextGeneration.getChromosomes().size(), DELTA);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        elitisticListPopulation.addChromosome(chromosome);
        assertTrue(elitisticListPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), DELTA);
    }

    @Test
    public void testChromosomeSort() {
        List<Chromosome> oldChromosomes = elitisticListPopulation.getChromosomes();
        oldChromosomes.get(0).setFitness(2.0);
        oldChromosomes.get(1).setFitness(1.0);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(oldChromosomes.get(0), newChromosomes.get(0));
        assertEquals(oldChromosomes.get(1), newChromosomes.get(1));
        assertTrue(newChromosomes.get(0).getFitness() > newChromosomes.get(1).getFitness());
    }

}