import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

  // Test case for ListPopulation(int populationLimit)
  @Test
  public void testListPopulation() {
    ListPopulation population = new ListPopulation(100);
    assertEquals(100, population.getPopulationLimit());
  }

  // Test case for ListPopulation(List<Chromosome> chromosomes, int populationLimit)
  @Test
  public void testListPopulationWithChromosomes() {
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    ListPopulation population = new ListPopulation(chromosomes, 100);
    assertEquals(2, population.getPopulationSize());
  }

  // Test case for setChromosomes(List<Chromosome> chromosomes)
  @Test
  public void testSetChromosomes() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    chromosomes.add(chromosome1);
    chromosomes.add(chromosome2);
    population.setChromosomes(chromosomes);
    assertEquals(2, population.getPopulationSize());
  }

  // Test case for addChromosomes(Collection<Chromosome> chromosomeColl)
  @Test
  public void testAddChromosomes() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    List<Chromosome> chromosomeColl = new ArrayList<Chromosome>();
    chromosomeColl.add(chromosome1);
    chromosomeColl.add(chromosome2);
    population.addChromosomes(chromosomeColl);
    assertEquals(2, population.getPopulationSize());
  }

  // Test case for getChromosomes()
  @Test
  public void testGetChromosomes() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    List<Chromosome> chromosomes = population.getChromosomes();
    assertEquals(2, chromosomes.size());
  }

  // Test case for getChromosomeList()
  @Test
  public void testGetChromosomeList() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    List<Chromosome> chromosomes = population.getChromosomeList();
    assertEquals(2, chromosomes.size());
  }

  // Test case for addChromosome(Chromosome chromosome)
  @Test
  public void testAddChromosome() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome = new Chromosome();
    population.addChromosome(chromosome);
    assertEquals(1, population.getPopulationSize());
  }

  // Test case for getFittestChromosome()
  @Test
  public void testGetFittestChromosome() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome(10);
    Chromosome chromosome2 = new Chromosome(20);
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    Chromosome fittestChromosome = population.getFittestChromosome();
    assertEquals(20, fittestChromosome.getFitness());
  }

  // Test case for getPopulationLimit()
  @Test
  public void testGetPopulationLimit() {
    ListPopulation population = new ListPopulation(100);
    assertEquals(100, population.getPopulationLimit());
  }

  // Test case for setPopulationLimit(int populationLimit)
  @Test
  public void testSetPopulationLimit() {
    ListPopulation population = new ListPopulation(100);
    population.setPopulationLimit(200);
    assertEquals(200, population.getPopulationLimit());
  }

  // Test case for getPopulationSize()
  @Test
  public void testGetPopulationSize() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    assertEquals(2, population.getPopulationSize());
  }

  // Test case for iterator()
  @Test
  public void testIterator() {
    ListPopulation population = new ListPopulation(100);
    Chromosome chromosome1 = new Chromosome();
    Chromosome chromosome2 = new Chromosome();
    population.addChromosome(chromosome1);
    population.addChromosome(chromosome2);
    Iterator<Chromosome> iterator = population.iterator();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertFalse(iterator.hasNext());
  }

}