// ElitisticListPopulationTest.java (Complete)

import org.apache.commons.math3.genetics.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

 @Test
 public void testElitisticListPopulationConstructorWithListAndLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 assertEquals(10, population.size());
 }

 @Test
 public void testElitisticListPopulationConstructorWithLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 assertEquals(10, population.size());
 }

 @Test
 public void testGetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 assertEquals(0.9, population.getElitismRate(), 0.01);
 }

 @Test
 public void testSetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 population.setElitismRate(0.8);
 assertEquals(0.8, population.getElitismRate(), 0.01);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateLowerThanZero() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 population.setElitismRate(-0.1);
 fail("Expected OutOfRangeException not thrown");
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateHigherThanOne() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
 population.setElitismRate(1.1);
 fail("Expected OutOfRangeException not thrown");
 }

 @Test
 public void testNextGenerationWithInitialPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(
 new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4),
 new DummyChromosome(5), new DummyChromosome(6), new DummyChromosome(7), new DummyChromosome(8),
 new DummyChromosome(9), new DummyChromosome(10)
 ), 15, 0.9);
 ElitisticListPopulation nextGeneration = population.nextGeneration();

 assertEquals(15, nextGeneration.size());

 List<DummyChromosome> oldChromosomes = population.getChromosomes();
 List<DummyChromosome> newChromosomes = nextGeneration.getChromosomes();

 for (int i = 0; i < 5; i++) {
 assertEquals(oldChromosomes.get(i), newChromosomes.get(i));
 }

 for (int i = 5; i < nextGeneration.size(); i++) {
 assertNotEquals(oldChromosomes.get(i), newChromosomes.get(i));
 }
 }

 @Test
 public void testNextGenerationWithEmptyPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 ElitisticListPopulation nextGeneration = population.nextGeneration();
 assertEquals(10, nextGeneration.size());
 }

 @Test
 public void testNextGenerationWithPopulationBiggerThanLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(
 new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4),
 new DummyChromosome(5), new DummyChromosome(6), new DummyChromosome(7), new DummyChromosome(8),
 new DummyChromosome(9), new DummyChromosome(11), new DummyChromosome(12)
 ), 10, 0.9);
 ElitisticListPopulation nextGeneration = population.nextGeneration();
 assertEquals(11, nextGeneration.size());
 }
}