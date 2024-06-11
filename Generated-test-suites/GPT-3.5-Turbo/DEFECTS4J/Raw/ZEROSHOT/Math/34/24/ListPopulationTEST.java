import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

  private ListPopulation listPopulation;

  @Before
  public void setUp() {
    listPopulation = new ListPopulation(100);
  }

  @Test(expected = NotPositiveException.class)
  public void testConstructor_NotPositivePopulationLimit() {
    new ListPopulation(0);
  }

  @Test
  public void testConstructor_ValidPopulationLimit() {
    ListPopulation listPopulation = new ListPopulation(100);
    assertNotNull(listPopulation);
  }

  @Test(expected = NullArgumentException.class)
  public void testConstructor_NullChromosomes() {
    new ListPopulation(null, 100);
  }

  @Test(expected = NotPositiveException.class)
  public void testConstructor_NotPositivePopulationLimitInList() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    new ListPopulation(chromosomes, 0);
  }

  @Test(expected = NumberIsTooLargeException.class)
  public void testConstructor_ListExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    new ListPopulation(chromosomes, 1);
  }

  @Test
  public void testConstructor_ValidChromosomesAndPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    ListPopulation listPopulation = new ListPopulation(chromosomes, 1);
    assertNotNull(listPopulation);
  }

  @Test(expected = NullArgumentException.class)
  public void testSetChromosomes_NullChromosomes() {
    listPopulation.setChromosomes(null);
  }

  @Test(expected = NumberIsTooLargeException.class)
  public void testSetChromosomes_ListExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    listPopulation.setChromosomes(chromosomes);
  }

  @Test
  public void testSetChromosomes_ValidChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    listPopulation.setChromosomes(chromosomes);
    assertEquals(chromosomes, listPopulation.getChromosomes());
  }

  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomes_ExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    listPopulation.addChromosomes(chromosomes);
  }

  @Test
  public void testAddChromosomes_ValidChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    listPopulation.addChromosomes(chromosomes);
    assertEquals(chromosomes, listPopulation.getChromosomes());
  }

  @Test
  public void testGetChromosomes() {
    List<Chromosome> chromosomes = listPopulation.getChromosomes();
    assertNotNull(chromosomes);
  }

  @Test
  public void testGetChromosomeList() {
    List<Chromosome> chromosomeList = listPopulation.getChromosomeList();
    assertNotNull(chromosomeList);
  }

  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosome_ExceedsPopulationLimit() {
    listPopulation.setPopulationLimit(1);
    listPopulation.addChromosome(new Chromosome());
  }

  @Test
  public void testAddChromosome_ValidChromosome() {
    Chromosome chromosome = new Chromosome();
    listPopulation.addChromosome(chromosome);
    assertTrue(listPopulation.getChromosomes().contains(chromosome));
  }

  @Test
  public void testGetFittestChromosome() {
    Chromosome chromosome1 = new Chromosome();
    chromosome1.setFitness(0.8);
    Chromosome chromosome2 = new Chromosome();
    chromosome2.setFitness(0.9);
    listPopulation.addChromosome(chromosome1);
    listPopulation.addChromosome(chromosome2);
    assertEquals(chromosome2, listPopulation.getFittestChromosome());
  }

  @Test
  public void testGetPopulationLimit() {
    assertEquals(100, listPopulation.getPopulationLimit());
  }

  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimit_NotPositivePopulationLimit() {
    listPopulation.setPopulationLimit(0);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimit_NewPopulationLimitSmallerThanChromosomes() {
    listPopulation.addChromosome(new Chromosome());
    listPopulation.setPopulationLimit(0);
  }

  @Test
  public void testSetPopulationLimit_ValidPopulationLimit() {
    listPopulation.setPopulationLimit(200);
    assertEquals(200, listPopulation.getPopulationLimit());
  }

  @Test
  public void testGetPopulationSize() {
    assertEquals(0, listPopulation.getPopulationSize());
    listPopulation.addChromosome(new Chromosome());
    assertEquals(1, listPopulation.getPopulationSize());
  }

  @Test
  public void testToString() {
    String str = listPopulation.toString();
    assertNotNull(str);
  }

  @Test
  public void testIterator() {
    Iterator<Chromosome> iterator = listPopulation.iterator();
    assertNotNull(iterator);
  }
}