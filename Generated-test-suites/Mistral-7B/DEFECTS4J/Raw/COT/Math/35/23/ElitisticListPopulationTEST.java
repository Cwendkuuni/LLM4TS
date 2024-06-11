import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // adding some chromosomes for testing
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Assert.assertEquals(5, population.size());
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        Assert.assertEquals(5, population.size());
    }

    @Test
    public void testNextGenerationWithSorting() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // adding some chromosomes for testing
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Collections.sort(population.getChromosomes());
        Population nextGeneration = population.nextGeneration();

        // the first elitismRate * size chromosomes should be the same
        for (int i = 0; i < (int) (population.size() * 0.9); i++) {
            Assert.assertEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }

        // the rest should be new chromosomes
        for (int i = population.size() * 0.9; i < nextGeneration.size(); i++) {
            Assert.assertNotSame(population.getChromosome(i), nextGeneration.getChromosome(i));
        }
    }

    @Test
    public void testNextGenerationWithoutSorting() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // adding some chromosomes for testing
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Population nextGeneration = population.nextGeneration();

        // the first elitismRate * size chromosomes should be the same
        for (int i = 0; i < (int) (population.size() * 0.9); i++) {
            Assert.assertEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }

        // the rest should be new chromosomes
        for (int i = population.size() * 0.9; i < nextGeneration.size(); i++) {
            Assert.assertNotSame(population.getChromosome(i), nextGeneration.getChromosome(i));
        }

        // sorting the new population and checking that the best chromosomes are the same
        Collections.sort(nextGeneration.getChromosomes());
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        for (int i = 0; i < (int) (population.size() * 0.9); i++) {
            Assert.assertEquals(oldChromosomes.get(i), nextGeneration.getChromosome(i));
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutsideRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        Assert.assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testSetAndGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.01);
    }
}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    public double[] getGenes() {
        return new double[]{id};
    }

    public int getSize() {
        return 1;
    }

    public String toString() {
        return "DummyChromosome" + id;
    }
}