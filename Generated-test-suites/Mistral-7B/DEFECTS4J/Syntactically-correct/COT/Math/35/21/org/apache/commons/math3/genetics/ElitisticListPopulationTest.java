import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = Collections.nemptyList();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Assert.assertEquals(populationLimit, population.size());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithLimit() {
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        Assert.assertEquals(populationLimit, population.size());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        List<Chromosome> chromosomes = Collections.emptyList();
        int populationLimit = 10;
        double elitismRate = -0.1;

        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = Collections.emptyList();
        int populationLimit = 10;
        double elitismRate = 1.1;

        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGenerationWithSorting() {
        List<Chromosome> chromosomes = Collections.nCopies(11, new DummyChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = population.getChromosome(i);
            Chromosome nextChromosome = nextGeneration.getChromosome(i);
            Assert.assertSame(chromosome, nextChromosome);
        }
    }

    @Test
    public void testNextGenerationWithShuffle() {
        List<Chromosome> chromosomes = Collections.nCopies(11, new DummyChromosome(0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Collections.shuffle(chromosomes);
        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(5, nextGeneration.size());
        boolean[] visited = new boolean[chromosomes.size()];
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = population.getChromosome(i);
            int index = chromosomes.indexOf(chromosome);
            Assert.assertFalse(visited[index]);
            visited[index] = true;
            Chromosome nextChromosome = nextGeneration.getChromosome(i);
            Assert.assertSame(chromosome, nextChromosome);
        }
    }

    private static class DummyChromosome implements Chromosome {
        private final double fitness;

        public DummyChromosome(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + fitness + '}';
        }
    }

}