import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));

        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Call the nextGeneration() method
        Population nextGeneration = population.nextGeneration();

        // Assert that the size of the next generation is correct
        assertEquals(3, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        // Create a population
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Set a new elitism rate
        population.setElitismRate(0.8);

        // Assert that the elitism rate has been updated
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    // Additional test cases for potential edge cases and exception handling scenarios:

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidRate() {
        // Create a population
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Set an invalid elitism rate
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidRate2() {
        // Create a population
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Set an invalid elitism rate
        population.setElitismRate(1.1);
    }

}