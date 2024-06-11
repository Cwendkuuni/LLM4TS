import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    // Method 1: ElitisticListPopulation(List<Chromosome> chromosomes, int populationLimit, double elitismRate)
    @Test
    public void testElitisticListPopulationConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.5, population.getElitismRate(), 0.0);
    }

    // Method 2: ElitisticListPopulation(int populationLimit, double elitismRate)
    @Test
    public void testElitisticListPopulationConstructor2() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.5, population.getElitismRate(), 0.0);
        Assert.assertEquals(0, population.getChromosomes().size());
    }

    // Method 3: nextGeneration()
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        chromosomes.add(c1);
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        Population nextGen = population.nextGeneration();

        List<Chromosome> nextGenChromosomes = nextGen.getChromosomes();
        Assert.assertEquals(2, nextGenChromosomes.size());
        Assert.assertFalse(nextGenChromosomes.contains(c1));
    }

    // Method 4: setElitismRate(double elitismRate)
    @Test
    public void testSetElitismRate_ValidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.7);

        Assert.assertEquals(0.7, population.getElitismRate(), 0.0);
    }

    // Method 4: setElitismRate(double elitismRate)
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRate_InvalidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

}