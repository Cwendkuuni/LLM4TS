import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.95);
        Chromosome bestChromosome = new FakeChromosome(1.0);
        chromosomes.add(bestChromosome);
        population.addChromosome(new FakeChromosome(0.5));
        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(10, nextGeneration.size());
        Assert.assertFalse(nextGeneration.contains(bestChromosome));
        for (int i = 0; i < 4; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Assert.assertTrue(nextGeneration.contains(chromosome));
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        population.setElitismRate(1.1);
    }

    private static class FakeChromosome implements Chromosome {

        private final double fitness;

        public FakeChromosome(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public double getFitness() {
            return fitness;
        }
    }
}