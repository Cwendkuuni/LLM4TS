import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {
  
  private ListPopulation listPopulation;
  
  @Before
  public void setUp() {
    listPopulation = new ListPopulation(10);
  }
  
  @Test
  public void testConstructor() {
    assertNotNull(listPopulation);
    assertEquals(10, listPopulation.getPopulationLimit());
    assertEquals(0, listPopulation.getPopulationSize());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testConstructor_NegativePopulationLimit() {
    listPopulation = new ListPopulation(-1);
  }
  
  @Test
  public void testAddChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    listPopulation.addChromosomes(chromosomes);
    assertEquals(2, listPopulation.getPopulationSize());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomes_ExceedPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    for (int i = 0; i < 11; i++) {
      chromosomes.add(new Chromosome());
    }
    listPopulation.addChromosomes(chromosomes);
  }
  
  @Test
  public void testGetChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    listPopulation.addChromosomes(chromosomes);
    List<Chromosome> result = listPopulation.getChromosomes();
    assertEquals(chromosomes, result);
  }
  
  @Test
  public void testGetChromosomeList() {
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    listPopulation.addChromosomes(chromosomes);
    List<Chromosome> result = listPopulation.getChromosomeList();
    assertEquals(chromosomes, result);
  }
  
  @Test
  public void testAddChromosome() {
    Chromosome chromosome = new Chromosome();
    listPopulation.addChromosome(chromosome);
    assertEquals(1, listPopulation.getPopulationSize());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosome_ExceedPopulationLimit() {
    for (int i = 0; i < 11; i++) {
      listPopulation.addChromosome(new Chromosome());
    }
  }
  
  @Test
  public void testGetFittestChromosome() {
    Chromosome chromosome1 = new Chromosome();
    chromosome1.setFitness(5);
    Chromosome chromosome2 = new Chromosome();
    chromosome2.setFitness(10);
    listPopulation.addChromosome(chromosome1);
    listPopulation.addChromosome(chromosome2);
    Chromosome result = listPopulation.getFittestChromosome();
    assertEquals(chromosome2, result);
  }
  
  @Test
  public void testGetPopulationLimit() {
    assertEquals(10, listPopulation.getPopulationLimit());
  }
  
  @Test
  public void testSetPopulationLimit() {
    listPopulation.setPopulationLimit(5);
    assertEquals(5, listPopulation.getPopulationLimit());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimit_NegativePopulationLimit() {
    listPopulation.setPopulationLimit(-1);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimit_SmallerThanCurrentSize() {
    for (int i = 0; i < 5; i++) {
      listPopulation.addChromosome(new Chromosome());
    }
    listPopulation.setPopulationLimit(3);
  }
  
  @Test
  public void testGetPopulationSize() {
    assertEquals(0, listPopulation.getPopulationSize());
  }
  
  @Test
  public void testToString() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    listPopulation.addChromosome(chromosome1);
    listPopulation.addChromosome(chromosome2);
    String result = listPopulation.toString();
    String expected = "[Chromosome@1, Chromosome@2]";
    assertEquals(expected, result);
  }
  
  @Test
  public void testIterator() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    listPopulation.addChromosome(chromosome1);
    listPopulation.addChromosome(chromosome2);
    Iterator<Chromosome> iterator = listPopulation.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(chromosome1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(chromosome2, iterator.next());
    assertFalse(iterator.hasNext());
  }
  
}