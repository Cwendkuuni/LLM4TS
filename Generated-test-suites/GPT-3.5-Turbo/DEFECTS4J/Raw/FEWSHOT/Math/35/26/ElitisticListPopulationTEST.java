import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithChromosomes() {
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        population = new ElitisticListPopulation(10, 0.5);
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfBounds() {
        population.setElitismRate(-0.1);
        fail("Expected an OutOfRangeException to be thrown");
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());
        assertEquals(population.getElitismRate(), nextGeneration.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGenerationChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);

        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        assertNotNull(nextGenerationChromosomes);
        assertEquals(chromosomes.size() - (int) Math.ceil((1.0 - population.getElitismRate()) * chromosomes.size()), nextGenerationChromosomes.size());
        assertTrue(nextGenerationChromosomes.contains(chromosome1));
        assertTrue(nextGenerationChromosomes.contains(chromosome2));
    }

}