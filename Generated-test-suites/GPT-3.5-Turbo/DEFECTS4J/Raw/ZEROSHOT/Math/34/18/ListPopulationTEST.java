import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {
  
  @Test
  public void testConstructor() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    assertEquals(chromosomes, listPopulation.getChromosomes());
    assertEquals(populationLimit, listPopulation.getPopulationLimit());
  }
  
  @Test(expected = NullArgumentException.class)
  public void testConstructorNullChromosomes() {
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(null, populationLimit);
  }
  
  @Test(expected = NotPositiveException.class)
  public void testConstructorNegativePopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = -10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testConstructorExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 1;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
  }
  
  @Test
  public void testSetChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.setChromosomes(chromosomes);
    
    assertEquals(chromosomes, listPopulation.getChromosomes());
  }
  
  @Test(expected = NullArgumentException.class)
  public void testSetChromosomesNullChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.setChromosomes(null);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testSetChromosomesExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 1;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.setChromosomes(chromosomes);
  }
  
  @Test
  public void testAddChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosomes(chromosomes);
    
    assertEquals(chromosomes, listPopulation.getChromosomes());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomesExceedsPopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 1;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosomes(chromosomes);
  }
  
  @Test
  public void testGetChromosomes() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    List<Chromosome> actualChromosomes = listPopulation.getChromosomes();
    
    assertEquals(chromosomes, actualChromosomes);
  }
  
  @Test
  public void testGetChromosomeList() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    List<Chromosome> actualChromosomes = listPopulation.getChromosomeList();
    
    assertEquals(chromosomes, actualChromosomes);
  }
  
  @Test
  public void testAddChromosome() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosome(new Chromosome());
    
    assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomePopulationLimit() {
    List<Chromosome> chromosomes = new ArrayList<>();
    int populationLimit = 1;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosome(new Chromosome());
    listPopulation.addChromosome(new Chromosome());
  }
  
  @Test
  public void testGetFittestChromosome() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosome(chromosome1);
    listPopulation.addChromosome(chromosome2);
    
    Chromosome fittestChromosome = listPopulation.getFittestChromosome();
    
    assertEquals(chromosome2, fittestChromosome);
  }
  
  @Test
  public void testGetPopulationLimit() {
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    
    assertEquals(populationLimit, listPopulation.getPopulationLimit());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimitNegativePopulationLimit() {
    int populationLimit = -10;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.setPopulationLimit(populationLimit);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimitSmallerThanCurrentPopulationSize() {
    int populationLimit = 1;
    
    ListPopulation listPopulation = new ListPopulation(populationLimit);
    listPopulation.addChromosome(new Chromosome());
    listPopulation.setPopulationLimit(populationLimit);
  }
  
  @Test
  public void testGetPopulationSize() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    int populationSize = listPopulation.getPopulationSize();
    
    assertEquals(chromosomes.size(), populationSize);
  }
  
  @Test
  public void testToString() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    String populationString = listPopulation.toString();
    
    assertEquals(chromosomes.toString(), populationString);
  }
  
  @Test
  public void testIterator() {
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(new Chromosome());
    chromosomes.add(new Chromosome());
    int populationLimit = 10;
    
    ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    
    Iterator<Chromosome> iterator = listPopulation.iterator();
    
    assertTrue(iterator.hasNext());
    assertEquals(chromosomes.get(0), iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(chromosomes.get(1), iterator.next());
    assertFalse(iterator.hasNext());
  }
}