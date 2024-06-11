import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ElitisticListPopulationTest {

 private List<Chromosome> createChromosomesList(int size) {
 return new java.util.ArrayList<Chromosome>(size);
 }

 @Test
 public void testElitisticListPopulationConstructionWithList() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
 assertNotNull(population);
 assertEquals(20, population.getPopulationLimit());
 assertEquals(0.5, population.getElitismRate(), 0.01);
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationConstructionWithNegativeElitismRate() {
 List<Chromosome> chromosomes = createChromosomesList(5);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, -0.1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationConstructionWithElitismRateGreaterThanOne() {
 List<Chromosome> chromosomes = createChromosomesList(5);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.2);
 }

 @Test
 public void testElitisticListPopulationConstructionWithPopulationLimit() {
 List<Chromosome> chromosomes = createChromosomesList(5);
 ElitisticListPopulation population = new ElitisticListPopulation(20, 0.5);
 assertNotNull(population);
 assertEquals(20, population.getPopulationLimit());
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationConstructionWithNegativePopulationLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(-5, 0.5);
 }

 @Test
 public void testNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertNotNull(nextGeneration);
 assertEquals(20, nextGeneration.getPopulationLimit());
 }

 @Test
 public void testElitismRateAfterNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(0.5, population.getElitismRate(), 0.01);
 }

 @Test
 public void testPopulationSizeAfterNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(20, population.size());
 assertEquals(20, nextGeneration.size());
 }

 @Test
 public void testPopulationContainsSameChromosomesBeforeAndAfterNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
 Population nextGeneration = population.nextGeneration();
 for (Chromosome oldChromosome : population) {
 Chromosome newChromosome = null;
 for (Chromosome nextChromosome : nextGeneration) {
 if (oldChromosome.getClass().isInstance(nextChromosome)) {
 newChromosome = nextChromosome;
 break;
 }
 }
 assertTrue(newChromosome != null);
 assertEquals(oldChromosome.getFitness(), newChromosome.getFitness(), 0.01);
 }
 }

 @Test
 public void testPopulationContainsElitismChromosomesAfterNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(12);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.6);
 Population nextGeneration = population.nextGeneration();Assert.assertEquals(15, nextGeneration.size());
 int elitismSize = (int) Math.ceil(population.size() * 0.6);
 int nonElitismSize = population.size() - elitismSize;
 int elitismIndex = 0;
 int nonElitismIndex = 0;
 for (Chromosome chromosome : nextGeneration) {
 if (elitismIndex < elitismSize) {
 Chromosome oldChromosome = population.get(elitismIndex);
 assertEquals(oldChromosome.getClass(), chromosome.getClass());
 elitismIndex++;
 } else {
 assertNotNull(chromosome);
 nonElitismIndex++;
 }
 }
 assertEquals(elitismSize, nextGeneration.size() - nonElitismSize);
 }

}