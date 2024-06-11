import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome(new double[]{1.0});
        Chromosome chromosome2 = new Chromosome(new double[]{2.0});
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Assert.assertEquals(5, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        Assert.assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(Collections.emptyList(), 5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(Collections.emptyList(), 5, 1.1);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.addChromosome(new Chromosome(new double[]{1.0}));
        population.addChromosome(new Chromosome(new double[]{2.0}));
        population.addChromosome(new Chromosome(new double[]{3.0}));
        population.addChromosome(new Chromosome(new double[]{4.0}));
        population.addChromosome(new Chromosome(new double[]{5.0}));

        Collections.sort(population.getChromosomes());

        List<Chromosome> firstHalf = population.getChromosomes().subList(0, (int) Math.ceil(population.size() * 0.1));
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        List<Chromosome> lastHalf = population.getChromosomes().subList((int) Math.ceil(population.size() * 0.9), population.size());
        List<Chromosome> newLastHalf = nextGeneration.getChromosomes().subList(population.size(), nextGeneration.size());

        for (Chromosome chromosome : firstHalf) {
            Assert.assertEquals(chromosome, nextGeneration.getChromosome(population.indexOf(chromosome)));
        }

        for (Chromosome chromosome : lastHalf) {
            Assert.assertNotEquals(chromosome, nextGeneration.getChromosome(population.indexOf(chromosome)));
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(1.1);
    }

}