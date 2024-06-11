import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

  @Test
  public void testNextGeneration() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome(1));
    chromosomes.add(new Chromosome(2));
    chromosomes.add(new Chromosome(3));
    chromosomes.add(new Chromosome(4));
    int populationLimit = 4;
    double elitismRate = 0.5;

    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    Population nextGeneration = population.nextGeneration();

    Assert.assertEquals("Next generation has incorrect population limit.", populationLimit, nextGeneration.getPopulationLimit());
    Assert.assertEquals("Next generation has incorrect elitism rate.", elitismRate, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.01);
    Assert.assertEquals("Next generation has incorrect number of chromosomes.", populationLimit, nextGeneration.getChromosomes().size());

    for (int i = 0; i < populationLimit; i++) {
      Assert.assertEquals("Next generation is missing chromosomes.", chromosomes.get(i), nextGeneration.getChromosomes().get(i));
    }
  }

  @Test
  public void testSetElitismRate() {
    double elitismRate = 0.5;
    double newElitismRate = 0.2;
    ElitisticListPopulation population = new ElitisticListPopulation(10, elitismRate);

    population.setElitismRate(newElitismRate);

    Assert.assertEquals("Elitism rate is not set correctly.", newElitismRate, population.getElitismRate(), 0.01);
  }

  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateOutOfRange() {
    double elitismRate = 0.5;
    double newElitismRate = 1.5;
    ElitisticListPopulation population = new ElitisticListPopulation(10, elitismRate);

    population.setElitismRate(newElitismRate);
  }

  @Test
  public void testGetElitismRate() {
    double elitismRate = 0.5;
    ElitisticListPopulation population = new ElitisticListPopulation(10, elitismRate);

    Assert.assertEquals("Elitism rate is not retrieved correctly.", elitismRate, population.getElitismRate(), 0.01);
  }

}