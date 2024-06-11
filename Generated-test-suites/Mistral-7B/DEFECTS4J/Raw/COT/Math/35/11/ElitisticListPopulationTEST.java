import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ElitisticListPopulationTest {

 private List<Chromosome> createChromosomesList(int size) {
 return new ArrayList<>(size);
 }

 @Test
 public void testElitisticListPopulationConstructorWithListAndLimit() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.9);
 assertNotNull(population);
 assertEquals(20, population.size());
 }

 @Test
 public void testElitisticListPopulationConstructorWithLimitAndRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 assertNotNull(population);
 assertEquals(10, population.size());
 }

 @Test
 public void testNextGenerationEmptyPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
 Population nextGeneration = population.nextGeneration();
 assertEquals(0, nextGeneration.size());
 }

 @Test
 public void testNextGenerationWithOneChromosome() {
 List<Chromosome> chromosomes = createChromosomesList(1);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
 Population nextGeneration = population.nextGeneration();
 assertEquals(1, nextGeneration.size());
 }

 @Test
 public void testNextGenerationWithMoreThanOneChromosome() {
 List<Chromosome> chromosomes = createChromosomesList(5);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
 Population nextGeneration = population.nextGeneration();
 int elitismPlaces = (int) Math.ceil((1.0 - 0.9) * chromosomes.size());
 assertEquals(elitismPlaces, population.size() - nextGeneration.size());
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateOutOfRange() {
 ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
 population.setElitismRate(-0.1);
 }

 @Test
 public void testSetElitismRateValidValue() {
 ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
 population.setElitismRate(0.5);
 assertEquals(0.5, population.getElitismRate(), 0.0);
 }

}