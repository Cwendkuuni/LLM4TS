import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testElitisticListPopulationConstructorWithValidArguments() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    /**
     * Test nextGeneration method with valid arguments.
     */
    @Test
    public void testNextGenerationWithValidArguments() {
        List<Chromosome> oldChromosomes = new ArrayList<>();
        oldChromosomes.add(new FakeChromosome(1));
        oldChromosomes.add(new FakeChromosome(2));
        oldChromosomes.add(new FakeChromosome(3));
        oldChromosomes.add(new FakeChromosome(4));
        oldChromosomes.add(new FakeChromosome(5));
        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.getPopulationSize());
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = oldChromosomes.get(i);
            Chromosome nextChromosome = nextGeneration.getChromosome(i);
            Assert.assertSame(chromosome, nextChromosome);
        }
    }

    /**
     * Test setElitismRate method with valid arguments.
     */
    @Test
    public void testSetElitismRateWithValidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid arguments.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(1.1);
    }

    /**
     * Fake Chromosome for testing purposes.
     */
    private static class FakeChromosome implements Chromosome {

        private int geneticMaterial;

        public FakeChromosome(int geneticMaterial) {
            this.geneticMaterial = geneticMaterial;
        }

        @Override
        public int getFitness() {
            return geneticMaterial;
        }

        @Override
        public String toString() {
            return "FakeChromosome{" +
                    "geneticMaterial=" + geneticMaterial +
                    '}';
        }
    }

}