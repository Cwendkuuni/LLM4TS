import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;

public class ListPopulationTest {
  
  @Test
  public void testConstructor() {
    ListPopulation population = new ListPopulation(100);
    
    Assert.assertEquals(0, population.getChromosomes().size());
    Assert.assertEquals(100, population.getPopulationLimit());
    Assert.assertEquals(0, population.getPopulationSize());
  }
  
  @Test
  public void testConstructorWithChromosomes() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    
    Assert.assertEquals(chromosomes, population.getChromosomes());
    Assert.assertEquals(100, population.getPopulationLimit());
    Assert.assertEquals(2, population.getPopulationSize());
  }
  
  @Test(expected = NullArgumentException.class)
  public void testConstructorWithNullChromosomes() {
    new ListPopulation(null, 100);
  }
  
  @Test(expected = NotPositiveException.class)
  public void testConstructorWithNegativePopulationLimit() {
    new ListPopulation(0);
  }
  
  @Test
  public void testSetChromosomes() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(100);
    population.setChromosomes(chromosomes);
    
    Assert.assertEquals(chromosomes, population.getChromosomes());
    Assert.assertEquals(100, population.getPopulationLimit());
    Assert.assertEquals(2, population.getPopulationSize());
  }
  
  @Test(expected = NullArgumentException.class)
  public void testSetChromosomesWithNullChromosomes() {
    ListPopulation population = new ListPopulation(100);
    population.setChromosomes(null);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testSetChromosomesWithExceedLimit() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(1);
    population.setChromosomes(chromosomes);
  }
  
  @Test
  public void testAddChromosomes() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(100);
    population.addChromosomes(chromosomes);
    
    Assert.assertEquals(chromosomes, population.getChromosomes());
    Assert.assertEquals(100, population.getPopulationLimit());
    Assert.assertEquals(2, population.getPopulationSize());
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testAddChromosomesWithExceedLimit() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(1);
    population.addChromosomes(chromosomes);
  }
  
  @Test
  public void testGetChromosomes() {
    ListPopulation population = new ListPopulation(100);
    
    Assert.assertEquals(0, population.getChromosomes().size());
  }
  
  @Test
  public void testGetFittestChromosome() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    chromosome1.setFitness(0.8);
    chromosome2.setFitness(0.6);
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    
    Assert.assertEquals(chromosome1, population.getFittestChromosome());
  }
  
  @Test
  public void testGetPopulationLimit() {
    ListPopulation population = new ListPopulation(100);
    
    Assert.assertEquals(100, population.getPopulationLimit());
  }
  
  @Test(expected = NotPositiveException.class)
  public void testSetPopulationLimitWithNegativeValue() {
    ListPopulation population = new ListPopulation(100);
    population.setPopulationLimit(0);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testSetPopulationLimitWithSmallerCurrentSize() {
    Chromosome chromosome = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    population.setPopulationLimit(0);
  }
  
  @Test
  public void testGetPopulationSize() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    
    Assert.assertEquals(2, population.getPopulationSize());
  }

  @Test
  public void testToString() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome = new Chromosome();
    population.addChromosome(chromosome);
    
    String expected = "[" + chromosome.toString() + "]";
    Assert.assertEquals(expected, population.toString());
  }
  
  @Test
  public void testIterator() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    
    Iterator<Chromosome> iterator = population.iterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(chromosome1, iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(chromosome2, iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testIteratorRemove() {
    Chromosome chromosome = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<>();
    chromosomes.add(chromosome);
    
    ListPopulation population = new ListPopulation(chromosomes, 100);
    
    Iterator<Chromosome> iterator = population.iterator();
    iterator.next();
    iterator.remove();
  }
  
}