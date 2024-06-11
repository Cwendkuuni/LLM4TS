import org.apache.commons.math3.ExceptionEasyBase;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));

        this.population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(this.population.getChromosomes(), 10, 0.5);
        Assert.assertEquals(this.population.getElitismRate(), population.getElitismRate());
        Assert.assertEquals(this.population.size(), population.size());
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        Assert.assertEquals(this.population.getElitismRate(), population.getElitismRate());
        Assert.assertEquals(this.population.size(), population.size());
    }

    @Test
    public void testNextGenerationWithSmallElitismRate() {
        Population nextGeneration = population.nextGeneration();
        int elitismSize = (int) Math.ceil(population.size() * 0.5);
        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        Assert.assertEquals(population.size() - elitismSize, nextGeneration.size());

        for (int i = 0; i < elitismSize; i++) {
            Chromosome elitismChromosome = oldChromosomes.get(i);
            Assert.assertArrayEquals(elitismChromosome.getGenes(), nextGenerationChromosomes.get(i).getGenes());
        }

        for (int i = elitismSize; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i - elitismSize);
            Assert.assertNotSame(oldChromosome, nextGenerationChromosome);
            Assert.assertNotEquals(oldChromosome.getFitness(), nextGenerationChromosome.getFitness());
        }
    }

    @Test
    public void testNextGenerationWithElitismRateOne() {
        population.setElitismRate(1);
        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(population.size(), nextGeneration.size());
        for (Chromosome chromosome : nextGeneration.getChromosomes()) {
            Chromosome populationChromosome = population.getChromosome(Collections.indexOf(population.getChromosomes(), chromosome));
            Assert.assertArrayEquals(populationChromosome.getGenes(), chromosome.getGenes());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-1);
    }

    @Test
    public void testGetElitismRate() {
        Assert.assertEquals(population.getElitismRate(), 0.5, 0.01);
    }

    private static class DummyChromosome implements Chromosome {

        private final double[] genes;

        public DummyChromosome(int i) {
            this.genes = new double[]{i + 1.0};
        }

        @Override
        public double[] getGenes() {
            return genes;
        }

        @Override
        public double getFitness() {
            return genes[0];
        }

        @Override
        public boolean isSolution() {
            return false;
        }
    }
}