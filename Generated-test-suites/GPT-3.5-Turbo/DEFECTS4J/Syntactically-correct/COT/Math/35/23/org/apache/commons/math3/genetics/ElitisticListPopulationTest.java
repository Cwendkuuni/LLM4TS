import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

  @Test
  public void testNextGeneration() {
    // Test case 1: Testing if the correct number of chromosomes are copied to the next generation
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    
    ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();
    assertEquals(9, nextGeneration.getChromosomes().size());
    
    // Test case 2: Testing if the chromosomes in the next generation are the best chromosomes from the current generation
    population = new ElitisticListPopulation(10, 0.9);
    Chromosome bestChromosome = new Chromosome();
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(new Chromosome());
    population.addChromosome(bestChromosome);
    
    nextGeneration = (ElitisticListPopulation) population.nextGeneration();
    assertTrue(nextGeneration.getChromosomes().contains(bestChromosome));
  }
  
  @Test
  public void testSetElitismRate() {
    // Test case 1: Testing if the elitism rate is set correctly
    ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
    population.setElitismRate(0.8);
    assertEquals(0.8, population.getElitismRate(), 0.0001);
    
    // Test case 2: Testing if an exception is thrown for values outside the [0, 1] range
    try {
      population.setElitismRate(-0.2);
      fail("Expected an OutOfRangeException to be thrown");
    } catch (OutOfRangeException e) {
      // Exception is expected
    }
    
    try {
      population.setElitismRate(1.2);
      fail("Expected an OutOfRangeException to be thrown");
    } catch (OutOfRangeException e) {
      // Exception is expected
    }
  }
}