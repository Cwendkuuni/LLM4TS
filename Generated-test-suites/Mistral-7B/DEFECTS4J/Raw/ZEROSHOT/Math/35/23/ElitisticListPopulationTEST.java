import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

 private List<Chromosome> createChromosomesList(int size) {
 return new java.util.ArrayList<Chromosome>(size);
 }

 @Test
 public void testElitisticListPopulationCreation() {
 //test minimum and maximum values for elitismRate
 ElitisticListPopulation population1 = new ElitisticListPopulation(createChromosomesList(10), 10, 0.0);
 ElitisticListPopulation population2 = new ElitisticListPopulation(createChromosomesList(10), 10, 1.0);
 ElitisticListPopulation population3 = new ElitisticListPopulation(10, 0.0);
 ElitisticListPopulation population4 = new ElitisticListPopulation(10, 1.0);

 assertEquals(10, population1.size());
 assertEquals(10, population2.size());
 assertEquals(10, population3.size());
 assertEquals(10, population4.size());
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationCreationWithOutOfRangeElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, 1.5);
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationCreationWithNegativeElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, -0.5);
 }

 @Test
 public void testNextGenerationWithInitialPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, 0.9);
 List<Chromosome> oldChromosomes = population.getChromosomes();
 Collections.sort(oldChromosomes);
 assertEquals(10, population.size());

 // sorting chromosomes and get elitism chromosomes
 List<Chromosome> newChromosomes = new ElitisticListPopulation(10, 0.9).nextGeneration().getChromosomes();
 List<Chromosome> expectedChromosomes = new java.util.ArrayList<Chromosome>(oldChromosomes.subList(0, (int) FastMath.ceil(oldChromosomes.size() * 0.9)));

 assertEquals(10, newChromosomes.size());
 for (int i = 0; i < expectedChromosomes.size(); i++) {
 assertEquals(expectedChromosomes.get(i), newChromosomes.get(i));
 }
 }

 @Test
 public void testNextGenerationWithEmptyPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(0), 10, 0.9);
 List<Chromosome> oldChromosomes = population.getChromosomes();
 assertEquals(0, oldChromosomes.size());

 // should throw an exception
 ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();
 }

 @Test
 public void testNextGenerationWithPopulationBiggerThanLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(12), 10, 0.9);
 List<Chromosome> oldChromosomes = population.getChromosomes();
 Collections.sort(oldChromosomes);

 assertEquals(12, population.size());
 List<Chromosome> newChromosomes = new ElitisticListPopulation(10, 0.9).nextGeneration().getChromosomes();

 assertEquals(10, newChromosomes.size());
 }

 @Test
 public void testSetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, 0.9);
 double oldElitismRate = population.getElitismRate();
 population.setElitismRate(0.5);
 double newElitismRate = population.getElitismRate();

 assertEquals(0.9, oldElitismRate, 0.01);
 assertEquals(0.5, newElitismRate, 0.01);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateWithOutOfRangeValue() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, 0.9);
 population.setElitismRate(1.5);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateWithNegativeValue() {
 ElitisticListPopulation population = new ElitisticListPopulation(createChromosomesList(10), 10, 0.9);
 population.setElitismRate(-0.5);
 }
}