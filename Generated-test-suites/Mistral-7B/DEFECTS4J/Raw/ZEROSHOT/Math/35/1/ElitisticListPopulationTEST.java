import org.apache.commons.math3.GeneticAlgorithm;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = createTestChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        Assert.assertEquals(20, population.size());
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        Assert.assertEquals(15, population.size());
    }

    /**
     * Test next generation with list and population limit.
     */
    @Test
    public void testNextGenerationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = createTestChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(15, nextGeneration.size());
    }

    /**
     * Test next generation with population limit.
     */
    @Test
    public void testNextGenerationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(15, nextGeneration.size());
    }

    /**
     * Test elitism rate setting.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Test elitism rate getting.
     */
    @Test
    public void testElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        Assert.assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test sorting and copying chromosomes to next generation.
     */
    @Test
    public void testSortingAndCopying() {
        List<Chromosome> chromosomes = createTestChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        Collections.sort(population.getChromosomes());

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        int boundIndex = (int) FastMath.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Assert.assertEquals(oldChromosomes.get(i), nextGenerationChromosomes.get(i));
        }
    }

    /**
     * Test getting elitism rate.
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        Assert.assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(15, 0.5);
        population.setElitismRate(0.7);
        Assert.assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    /**
     * Create test chromosomes.
     *
     * @param numberOfChromosomes the number of chromosomes to create
     * @return list of test chromosomes
     */
    private List<Chromosome> createTestChromosomes(int numberOfChromosomes) {
        GeneticAlgorithm ga = new GeneticAlgorithm(new SimpleGAConfig(5, 10, 1, 100));
        return ga.getPopulation().getChromosomes();
    }
}