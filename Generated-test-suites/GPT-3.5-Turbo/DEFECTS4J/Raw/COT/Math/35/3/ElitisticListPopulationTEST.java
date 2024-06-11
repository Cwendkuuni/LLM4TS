import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor1() {
        // Test constructor with a list of chromosomes, population limit, and elitism rate
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        int populationLimit = 100;
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Check if the chromosomes, populationLimit, and elitismRate are set correctly
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testConstructor2() {
        // Test constructor with only population limit and elitism rate
        int populationLimit = 100;
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        // Check if the populationLimit and elitismRate are set correctly
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testNextGeneration() {
        // Test the nextGeneration() method
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        int populationLimit = 100;
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        // Check if the next generation has the correct number of chromosomes
        assertEquals(chromosomes.size() - (int) (chromosomes.size() * elitismRate), nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        // Test the setElitismRate() method
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);

        // Check if the elitism rate is set correctly
        assertEquals(newElitismRate, population.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Test the setElitismRate() method with an out of range elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        double newElitismRate = 1.5;
        population.setElitismRate(newElitismRate);
    }

}