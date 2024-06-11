import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.exception.OutOfRangeException;


public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Test constructor with list of chromosomes
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(chromosome1, chromosome2), 100, 0.8);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Test constructor without chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.8);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        // Test the nextGeneration method
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(chromosome1, chromosome2), 100, 0.8);
        Population nextGeneration = population.nextGeneration();
        assertEquals(80, nextGeneration.getPopulationLimit());
        assertEquals(0.8, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOutOfRange() {
        // Test setting elitism rate outside the [0, 1] range
        ElitisticListPopulation population = new ElitisticListPopulation(100, 1.5);
    }

}