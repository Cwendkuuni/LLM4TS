import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ListPopulationTest {

   @Test
   public void testSetChromosomes() {
       List<Chromosome> chromosomes = new ArrayList<>();
       ListPopulation population = new ListPopulation(10);
       population.setChromosomes(chromosomes);
   }

   @Test(expected = NullArgumentException.class)
   public void testSetChromosomesNullChromosomes() {
       ListPopulation population = new ListPopulation(10);
       population.setChromosomes(null);
   }

   @Test(expected = NumberIsTooLargeException.class)
   public void testSetChromosomesExceedPopulationLimit() {
       List<Chromosome> chromosomes = new ArrayList<>();
       chromosomes.add(new Chromosome());
       ListPopulation population = new ListPopulation(0);
       population.setChromosomes(chromosomes);
   }

   @Test
   public void testAddChromosomes() {
       List<Chromosome> chromosomes = new ArrayList<>();
       ListPopulation population = new ListPopulation(10);
       population.addChromosomes(chromosomes);
   }

   @Test(expected = NumberIsTooLargeException.class)
   public void testAddChromosomesExceedPopulationLimit() {
       List<Chromosome> chromosomes = new ArrayList<>();
       chromosomes.add(new Chromosome());
       ListPopulation population = new ListPopulation(0);
       population.addChromosomes(chromosomes);
   }

   @Test
   public void testGetChromosomes() {
       ListPopulation population = new ListPopulation(10);
       List<Chromosome> chromosomes = population.getChromosomes();
       assert(chromosomes != null);
   }

   @Test
   public void testGetFittestChromosome() {
       ListPopulation population = new ListPopulation(10);
       Chromosome fittestChromosome = population.getFittestChromosome();
       assert(fittestChromosome != null);
   }

   @Test
   public void testGetPopulationLimit() {
       ListPopulation population = new ListPopulation(10);
       int populationLimit = population.getPopulationLimit();
       assert(populationLimit == 10);
   }

   @Test(expected = NotPositiveException.class)
   public void testSetPopulationLimitNotPositive() {
       ListPopulation population = new ListPopulation(10);
       population.setPopulationLimit(-1);
   }

   @Test(expected = NumberIsTooSmallException.class)
   public void testSetPopulationLimitSmallerThanCurrentSize() {
       ListPopulation population = new ListPopulation(10);
       population.addChromosomes(Collections.nCopies(10, new Chromosome()));
       population.setPopulationLimit(5);
   }

   @Test
   public void testGetPopulationSize() {
       ListPopulation population = new ListPopulation(10);
       int populationSize = population.getPopulationSize();
       assert(populationSize == 0);
   }

   @Test
   public void testIterator() {
       ListPopulation population = new ListPopulation(10);
       Iterator<Chromosome> iterator = population.iterator();
       assert(iterator != null);
   }
}