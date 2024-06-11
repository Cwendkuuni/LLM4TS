
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

  private ElitisticListPopulation population;

  @Before
  public void setUp() {
     List<Chromosome> chromosomes = new ArrayList<>();
     chromosomes.add(new Chromosome());
     population = new ElitisticListPopulation(chromosomes, 10, 0.9);
  }

  @Test
  public void testNextGeneration() {
    Population nextGeneration = population.nextGeneration();
    assertNotNull(nextGeneration);
    assertEquals(9, nextGeneration.getChromosomes().size());
  }

  @Test
  public void testSetElitismRate() {
    double elitismRate = 0.8;
    population.setElitismRate(elitismRate);
    assertEquals(elitismRate, population.getElitismRate(), 0.0);
  }

  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateOutOfRange() {
    double elitismRate = 1.5;
    population.setElitismRate(elitismRate);
  }

}
