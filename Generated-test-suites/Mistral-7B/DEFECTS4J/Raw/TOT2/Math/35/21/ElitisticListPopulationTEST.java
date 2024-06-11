// ElitisticListPopulationTest.java (Complete)

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

 @Test
 public void testElitisticListPopulationConstructorWithListAndLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.singletonList(new DummyChromosome()), 10, 0.5);
 assertEquals(10, population.size());
 }

 @Test
 public void testElitisticListPopulationConstructorWithLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 assertEquals(10, population.size());
 }

 @Test
 public void testGetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 assertEquals(0.5, population.getElitismRate(), 0.01);
 }

 @Test
 public void testSetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 population.setElitismRate(0.6);
 assertEquals(0.6, population.getElitismRate(), 0.01);
 }

 @Test
 public void testNextGenerationWithEmptyPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(10, nextGeneration.size());
 }

 @Test
 public void testNextGenerationWithPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new DummyChromosome(), new DummyChromosome(), new DummyChromosome()), 10, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(10, nextGeneration.size());
 }

 @Test(expected = OutOfRangeException.class)
 public void testNextGenerationWithNegativeElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new DummyChromosome(), new DummyChromosome(), new DummyChromosome()), 10, -0.1);
 population.nextGeneration();
 }

 @Test
 public void testNextGenerationWithValidElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new DummyChromosome(), new DummyChromosome(), new DummyChromosome()), 10, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(10, nextGeneration.size());
 }

 @Test(expected = OutOfRangeException.class)
 public void testConstructorWithInvalidElitismRate() {
 assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(Collections.singletonList(new DummyChromosome()), 10, 1.1));
 }

 @Test
 public void testNextGenerationEdgeCases() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new DummyChromosome(), new DummyChromosome(), new DummyChromosome()), 3, 0.5);
 Population nextGeneration = population.nextGeneration();
 assertEquals(3, nextGeneration.size());
 nextGeneration = population.nextGeneration();
 assertEquals(3, nextGeneration.size());
 nextGeneration = population.nextGeneration();
 assertEquals(3, nextGeneration.size());
 }

 @Test
 public void testSetElitismRateEdgeCases() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new DummyChromosome(), new DummyChromosome(), new DummyChromosome()), 10, 0.5);
 population.setElitismRate(1.2);
 assertEquals(0.5, population.getElitismRate(), 0.01);
 }

}