
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
  private ListPopulation population;
  private Chromosome chromosome1;
  private Chromosome chromosome2;
  
  @Before
  public void setUp() {
    population = new ListPopulation(2);
    chromosome1 = new Chromosome();
    chromosome2 = new Chromosome();
  }
  
  @Test
  public void testConstructor() {
    assertEquals(0, population.getPopulationSize());
    assertEquals(2, population.getPopulationLimit());
  }
  
  @Test
  public void testAddChromosomes() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    assertEquals(2, population.getPopulationSize());
    assertEquals(chromosome1, population.iterator().next());
  }
  
  @Test
  public void testSetChromosomes() {
    population.setChromosomes(Arrays.asList(chromosome1, chromosome2));
    assertEquals(2, population.getPopulationSize());
    assertEquals(chromosome1, population.iterator().next());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomesExceedsPopulationLimit() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2, new Chromosome()));
  }
  
  @Test
  public void testAddChromosome() {
    population.addChromosome(chromosome1);
    assertEquals(1, population.getPopulationSize());
    assertEquals(chromosome1, population.iterator().next());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomeExceedsPopulationLimit() {
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    population.addChromosome(new Chromosome());
  }
  
  @Test
  public void testGetFittestChromosome() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    assertEquals(chromosome1, population.getFittestChromosome());
  }
  
  @Test
  public void testGetPopulationLimit() {
    assertEquals(2, population.getPopulationLimit());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimitNotPositive() {
    population.setPopulationLimit(0);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimitTooSmall() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    population.setPopulationLimit(1);
  }
  
  @Test
  public void testSetPopulationLimit() {
    population.setPopulationLimit(3);
    assertEquals(3, population.getPopulationLimit());
  }
  
  @Test
  public void testGetPopulationSize() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    assertEquals(2, population.getPopulationSize());
  }
  
  @Test
  public void testIterator() {
    population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    Iterator<Chromosome> iterator = population.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(chromosome1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(chromosome2, iterator.next());
    assertFalse(iterator.hasNext());
  }
}
