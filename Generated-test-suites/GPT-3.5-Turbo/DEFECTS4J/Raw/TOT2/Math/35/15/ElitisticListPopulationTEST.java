import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(1, population.getChromosomes().size());
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0, population.getChromosomes().size());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.5);
    }

    // Add more test cases for edge cases and exception handling scenarios as needed

}