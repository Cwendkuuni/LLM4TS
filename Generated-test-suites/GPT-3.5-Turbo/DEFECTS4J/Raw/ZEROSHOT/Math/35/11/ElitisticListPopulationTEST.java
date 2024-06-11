import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

  private ElitisticListPopulation population;

  @Before
  public void setUp() {
    population = new ElitisticListPopulation(10, 0.9);
  }

  @Test
  public void testNextGeneration() {
    population.setElitismRate(0.8);
    Population nextGeneration = population.nextGeneration();
    assertTrue(nextGeneration instanceof ElitisticListPopulation);
    ElitisticListPopulation elitisticNextGeneration = (ElitisticListPopulation) nextGeneration;
    assertEquals(population.getPopulationLimit(), elitisticNextGeneration.getPopulationLimit());
    assertEquals(population.getElitismRate(), elitisticNextGeneration.getElitismRate(), 0.0001);
    assertEquals(population.getChromosomes().size() - (int) Math.ceil((1 - population.getElitismRate()) * population.getChromosomes().size()), elitisticNextGeneration.getChromosomes().size());
  }

  @Test
  public void testSetElitismRate() {
    double elitismRate = 0.5;
    population.setElitismRate(elitismRate);
    assertEquals(elitismRate, population.getElitismRate(), 0.0001);
  }

  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateNegative() {
    population.setElitismRate(-0.1);
  }

  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRateGreaterThanOne() {
    population.setElitismRate(1.1);
  }

  @Test
  public void testGetElitismRate() {
    double elitismRate = 0.9;
    assertEquals(elitismRate, population.getElitismRate(), 0.0001);
  }
}