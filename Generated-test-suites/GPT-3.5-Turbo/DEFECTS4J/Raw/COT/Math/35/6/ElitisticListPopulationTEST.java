import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    // Test constructor with specified chromosomes, population limit, and elitism rate
    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
        assertEquals(chromosomes.size(), population.getChromosomes().size());
    }

    // Test constructor with specified population limit and elitism rate
    @Test
    public void testElitisticListPopulation2() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
        assertEquals(0, population.getChromosomes().size());
    }

    // Test nextGeneration method
    @Test
    public void testNextGeneration() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(chromosome1, chromosome2, chromosome3), 100, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertEquals(100, nextGeneration.getPopulationLimit());
        assertEquals(0.9, ((ElitisticListPopulation)nextGeneration).getElitismRate(), 0.01);
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().contains(chromosome2));
        assertTrue(nextGeneration.getChromosomes().contains(chromosome3));
        assertFalse(nextGeneration.getChromosomes().contains(chromosome1));
    }

    // Test setElitismRate method
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    // Test setElitismRate method with out of range elitismRate
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(1.1);
    }

    // Test getElitismRate method
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }
}