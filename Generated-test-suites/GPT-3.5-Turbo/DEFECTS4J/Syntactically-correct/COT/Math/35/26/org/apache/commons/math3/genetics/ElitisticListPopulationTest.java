
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

  @Test
  public void testNextGeneration() {
    // Test with a populationLimit of 10 and elitismRate of 0.9
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
    // Add some dummy chromosomes to the population
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);

    // Perform nextGeneration() and check if the returned population has the correct size
    ElitisticListPopulation nextGeneration = population.nextGeneration();
    assertEquals(3, nextGeneration.getChromosomes().size());
  }

  @Test
  public void testSetElitismRate() {
    // Test setting the elitismRate to 0.5
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
    population.setElitismRate(0.5);
    assertEquals(0.5, population.getElitismRate(), 0.001);
  }

}
