import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ElitisticListPopulationTest {

  @Test
  public void testNextGeneration() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    Chromosome c1 = new Chromosome();
    chromosomes.add(c1);
    Chromosome c2 = new Chromosome();
    chromosomes.add(c2);
    Chromosome c3 = new Chromosome();
    chromosomes.add(c3);
    Chromosome c4 = new Chromosome();
    chromosomes.add(c4);
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    Population nextGeneration = population.nextGeneration();
    assertEquals(4, population.getChromosomes().size());
    assertEquals(1, nextGeneration.getChromosomes().size());
  }

  @Test
  public void testSetElitismRate() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    population.setElitismRate(0.5);
    assertEquals(0.5, population.getElitismRate(), 0.001);
  }

  @Test(expected = OutOfRangeException.class)
  public void testSetElitismRate_OutOfRangeException() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    population.setElitismRate(1.5);
  }

}