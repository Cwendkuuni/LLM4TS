import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        population = new ElitisticListPopulation(10, 0.9);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation newPopulation = new ElitisticListPopulation(chromosomes, 20, 0.8);

        assertEquals(3, newPopulation.getChromosomes().size());
        assertEquals(20, newPopulation.getPopulationLimit());
        assertEquals(0.8, newPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithPopulationLimit() {
        ElitisticListPopulation newPopulation = new ElitisticListPopulation(10, 0.8);

        assertEquals(0, newPopulation.getChromosomes().size());
        assertEquals(10, newPopulation.getPopulationLimit());
        assertEquals(0.8, newPopulation.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowZero() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveOne() {
        population.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome());
        }

        ListPopulation nextGeneration = population.nextGeneration();

        assertEquals(1, nextGeneration.getPopulationSize());
        assertNotNull(nextGeneration.getChromosome(0));
    }

    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ListPopulation nextGeneration = population.nextGeneration();

        assertEquals(0, nextGeneration.getPopulationSize());
    }

    @Test
    public void testNextGenerationWithSingleChromosome() {
        population.addChromosome(new Chromosome());

        ListPopulation nextGeneration = population.nextGeneration();

        assertEquals(1, nextGeneration.getPopulationSize());
        assertNotNull(nextGeneration.getChromosome(0));
    }

    @Test
    public void testNextGenerationWithElitismRateEqualToOne() {
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome());
        }

        population.setElitismRate(1.0);
        ListPopulation nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.getPopulationSize());
        for (int i = 0; i < 10; i++) {
            assertNotNull(nextGeneration.getChromosome(i));
        }
    }

    // Additional test cases may be added to further validate the functionality of the ElitisticListPopulation class.
}