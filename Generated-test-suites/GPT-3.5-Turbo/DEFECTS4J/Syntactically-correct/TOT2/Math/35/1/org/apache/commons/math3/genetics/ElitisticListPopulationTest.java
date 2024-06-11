import static org.junit.Assert.*;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor1() {
        // Test constructor with list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertNotNull(population);
        assertEquals(chromosomes.size(), population.getChromosomes().size());
    }

    @Test
    public void testElitisticListPopulationConstructor2() {
        // Test constructor without list of chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertNotNull(population);
        assertEquals(0, population.getChromosomes().size());
    }

    @Test
    public void testNextGeneration() {
        // Test nextGeneration method
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());
    }

    @Test
    public void testSetElitismRate() {
        // Test setElitismRate method with valid rate
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Test setElitismRate method with invalid rate
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }
}