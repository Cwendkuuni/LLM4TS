import org.apache.commons.math3.genetics.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    // Test case for ElitisticListPopulation(List<Chromosome> chromosomes, int populationLimit, double elitismRate)

    @Test
    public void testConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 5;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    // Test case for ElitisticListPopulation(int populationLimit, double elitismRate)

    @Test
    public void testConstructor2() {
        int populationLimit = 10;
        double elitismRate = 0.3;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertEquals(0, population.getChromosomes().size());
    }

    // Test case for nextGeneration()

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(10));
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(7));
        int populationLimit = 2;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        // Verify that the population limit is maintained
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());

        // Verify that the elitism rate is maintained
        assertEquals(elitismRate, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);

        // Verify that the best chromosomes are copied directly to the next generation
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(7));
        expectedChromosomes.add(new Chromosome(10));
        assertEquals(expectedChromosomes, nextGeneration.getChromosomes());
    }

    // Test cases for setElitismRate(double elitismRate)

    @Test
    public void testSetElitismRate() {
        int populationLimit = 5;
        double elitismRate = 0.8;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(elitismRate);

        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        int populationLimit = 5;
        double elitismRate = 1.5;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(elitismRate);
    }

    // Test case for getElitismRate()

    @Test
    public void testGetElitismRate() {
        int populationLimit = 10;
        double elitismRate = 0.3;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

}