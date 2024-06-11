import static org.junit.Assert.*;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Test constructor with list of chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<Chromosome>(), 10, 0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
        assertEquals(0, population.getChromosomes().size());
    }

    @Test
    public void testElitisticListPopulationConstructorPopulationLimit() {
        // Test constructor with only population limit
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
        assertEquals(0, population.getChromosomes().size());
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        // Test nextGeneration method
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        // Test setElitismRate method
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<Chromosome>(), 10, 0.8);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Test setElitismRate method with out of range value
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<Chromosome>(), 10, 0.8);
        population.setElitismRate(-0.5);
    }
}