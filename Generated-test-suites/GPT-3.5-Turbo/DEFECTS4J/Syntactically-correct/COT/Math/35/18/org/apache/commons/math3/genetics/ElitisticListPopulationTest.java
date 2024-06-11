import org.apache.commons.math3.genetics.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

  // Test case for ElitisticListPopulation(List<Chromosome> chromosomes, int populationLimit, double elitismRate)
  @Test
  public void testElitisticListPopulation1() {
    // Create a list of chromosomes
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());

    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

    // Test population size
    assertEquals(2, population.getPopulationLimit());

    // Test elitism rate
    assertEquals(0.9, population.getElitismRate(), 0.0001);
  }

  // Test case for ElitisticListPopulation(int populationLimit, double elitismRate)
  @Test
  public void testElitisticListPopulation2() {
    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

    // Test population size
    assertEquals(10, population.getPopulationLimit());

    // Test elitism rate
    assertEquals(0.9, population.getElitismRate(), 0.0001);
  }

  // Test case for nextGeneration()
  @Test
  public void testNextGeneration() {
    // Create a list of chromosomes
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome(1));
    chromosomes.add(new Chromosome(2));
    chromosomes.add(new Chromosome(3));
    chromosomes.add(new Chromosome(4));
    chromosomes.add(new Chromosome(5));

    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.6);

    // Create next generation
    Population nextGeneration = population.nextGeneration();

    // Test population size
    assertEquals(3, nextGeneration.getPopulationSize());

    // Test if best chromosomes are copied to the next generation
    assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(4)));
    assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(5)));
  }

  // Test case for setElitismRate(double elitismRate)
  @Test
  public void testSetElitismRate() {
    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

    // Set elitism rate
    population.setElitismRate(0.8);

    // Test elitism rate
    assertEquals(0.8, population.getElitismRate(), 0.0001);
  }

  // Test case for high elitism rate
  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateHigh() {
    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

    // Set elitism rate (outside the [0, 1] range)
    population.setElitismRate(1.2);
  }

  // Test case for low elitism rate
  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateLow() {
    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

    // Set elitism rate (outside the [0, 1] range)
    population.setElitismRate(-0.2);
  }

  // Test case for getElitismRate()
  @Test
  public void testGetElitismRate() {
    // Create ElitisticListPopulation object
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

    // Test elitism rate
    assertEquals(0.9, population.getElitismRate(), 0.0001);
  }

}