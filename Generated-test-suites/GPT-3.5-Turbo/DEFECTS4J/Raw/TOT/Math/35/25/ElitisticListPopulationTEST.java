import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {
  
  private ElitisticListPopulation elitisticListPopulation;
  
  @Before
  public void setUp() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.5);
  }
  
  @Test
  public void testElitisticListPopulation_Population() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.3);
    
    assertEquals(20, population.getPopulationLimit());
    assertEquals(0.3, population.getElitismRate(), 0.00001);
    assertEquals(3, population.getChromosomes().size());
  }
  
  @Test
  public void testElitisticListPopulation_LimitAndRate() {
    ElitisticListPopulation population = new ElitisticListPopulation(50, 0.7);
    
    assertEquals(50, population.getPopulationLimit());
    assertEquals(0.7, population.getElitismRate(), 0.00001);
    assertEquals(0, population.getChromosomes().size());
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testElitisticListPopulation_InvalidElitismRate() {
    new ElitisticListPopulation(100, 1.5);
  }
  
  @Test
  public void testNextGeneration() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.6);
    
    Population nextGeneration = population.nextGeneration();
    
    assertEquals(5, nextGeneration.getPopulationLimit());
    assertEquals(0.6, nextGeneration.getElitismRate(), 0.00001);
  }
  
  @Test
  public void testSetElitismRate() {
    elitisticListPopulation.setElitismRate(0.8);
    assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.00001);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRate_InvalidRate() {
    elitisticListPopulation.setElitismRate(1.2);
  }
  
  @Test
  public void testGetElitismRate() {
    assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.00001);
  }
}