import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {
  
  private ListPopulation listPopulation;
  private List<Chromosome> chromosomes;
  
  @Before
  public void setup() {
    chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    
    listPopulation = new ListPopulation(chromosomes, 5);
  }
  
  @Test
  public void testConstructor() {
    assertEquals(5, listPopulation.getPopulationLimit());
    assertEquals(chromosomes, listPopulation.getChromosomeList());
  }
  
  @Test
  public void testSetChromosomes() {
    List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
    newChromosomes.add(new Chromosome());
    newChromosomes.add(new Chromosome());
    
    listPopulation.setChromosomes(newChromosomes);
    
    assertEquals(newChromosomes, listPopulation.getChromosomeList());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testSetChromosomesThrowsNumberIsTooLargeException() {
    List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
    newChromosomes.add(new Chromosome());
    newChromosomes.add(new Chromosome());
    newChromosomes.add(new Chromosome());
    
    listPopulation.setChromosomes(newChromosomes);
  }
  
  @Test
  public void testAddChromosomes() {
    List<Chromosome> additionalChromosomes = new ArrayList<Chromosome>();
    additionalChromosomes.add(new Chromosome());
    additionalChromosomes.add(new Chromosome());
    
    listPopulation.addChromosomes(additionalChromosomes);
    
    List<Chromosome> expectedChromosomes = new ArrayList<Chromosome>(chromosomes);
    expectedChromosomes.addAll(additionalChromosomes);
    
    assertEquals(expectedChromosomes, listPopulation.getChromosomeList());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomesThrowsNumberIsTooLargeException() {
    List<Chromosome> additionalChromosomes = new ArrayList<Chromosome>();
    additionalChromosomes.add(new Chromosome());
    additionalChromosomes.add(new Chromosome());
    additionalChromosomes.add(new Chromosome());
    
    listPopulation.addChromosomes(additionalChromosomes);
  }
  
  @Test
  public void testGetChromosomes() {
    assertEquals(chromosomes, listPopulation.getChromosomes());
  }
  
  @Test
  public void testGetFittestChromosome() {
    Chromosome fittestChromosome = new Chromosome();
    chromosomes.add(fittestChromosome);
    
    assertEquals(fittestChromosome, listPopulation.getFittestChromosome());
  }
  
  @Test
  public void testGetPopulationLimit() {
    assertEquals(5, listPopulation.getPopulationLimit());
  }
  
  @Test
  public void testSetPopulationLimit() {
    listPopulation.setPopulationLimit(10);
    
    assertEquals(10, listPopulation.getPopulationLimit());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimitThrowsNotPositiveException() {
    listPopulation.setPopulationLimit(-1);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimitThrowsNumberIsTooSmallException() {
    listPopulation.setPopulationLimit(2);
  }
  
  @Test
  public void testGetPopulationSize() {
    assertEquals(3, listPopulation.getPopulationSize());
  }
  
  @Test
  public void testToString() {
    assertEquals(chromosomes.toString(), listPopulation.toString());
  }
  
  @Test
  public void testIterator() {
    Iterator<Chromosome> iterator = listPopulation.iterator();
    
    assertTrue(iterator.hasNext());
    assertEquals(chromosomes.get(0), iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(chromosomes.get(1), iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(chromosomes.get(2), iterator.next());
    assertFalse(iterator.hasNext());
    }
  }