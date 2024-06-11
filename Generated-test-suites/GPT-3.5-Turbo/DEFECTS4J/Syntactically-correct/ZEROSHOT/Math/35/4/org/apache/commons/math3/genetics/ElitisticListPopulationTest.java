import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomesPopulationLimitAndElitismRate() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Check if chromosomes are correctly initialized
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Check if population limit and elitism rate are correctly initialized
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));

        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.6);

        // Call the nextGeneration() method
        Population nextGeneration = population.nextGeneration();

        // Check if the size of the next generation is correct
        assertEquals(3, nextGeneration.getChromosomes().size());

        // Check if the chromosomes in the next generation are correct
        assertEquals(new Chromosome(4), nextGeneration.getChromosomes().get(0));
        assertEquals(new Chromosome(5), nextGeneration.getChromosomes().get(1));
        assertEquals(new Chromosome(3), nextGeneration.getChromosomes().get(2));
    }

    @Test
    public void testSetElitismRate() {
        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Change the elitism rate
        population.setElitismRate(0.8);

        // Check if the elitism rate is correctly changed
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }
}