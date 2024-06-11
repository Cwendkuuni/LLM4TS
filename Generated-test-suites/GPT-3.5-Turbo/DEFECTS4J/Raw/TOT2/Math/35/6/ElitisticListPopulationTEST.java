import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    // Test case for constructor with chromosomes, population limit, and elitism rate
    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
        assertEquals(1, population.getChromosomes().size());
    }

    // Test case for constructor with only population limit and elitism rate
    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
        assertEquals(0, population.getChromosomes().size());
    }

    // Test case for nextGeneration method
    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
        Population nextGen = population.nextGeneration();
        assertEquals(1, nextGen.getChromosomes().size());
    }

    // Test case for setting elitism rate within range
    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    // Test case for setting elitism rate outside range
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutsideRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.5);
    }

}