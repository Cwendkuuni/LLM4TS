import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Test constructor with chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("123"));
        chromosomes.add(new Chromosome("456"));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Test constructor without chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);

        assertNotNull(population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testNextGeneration() {
        // Test next generation method
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("456"));
        chromosomes.add(new Chromosome("123"));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 2, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(1, nextGeneration.getChromosomes().size());
        assertEquals("123", nextGeneration.getChromosomes().get(0).toString());
    }

    @Test
    public void testSetElitismRate() {
        // Test set elitism rate method
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        double newElitismRate = 0.7;
        population.setElitismRate(newElitismRate);

        assertEquals(newElitismRate, population.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Test set elitism rate method with out of range value
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);
    }
}