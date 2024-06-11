// ElitisticListPopulationTest.java (Complete)
package org.apache.commons.math3.genetics;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

 // Test constructors with valid parameters
 @Test
 public void testConstructorWithValidParameters() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 assertEquals(10, population.size());
 }

 // Test constructor with invalid elitismRate
 @Test(expected = OutOfRangeException.class)
 public void testConstructorWithInvalidElitismRate() {
 new ElitisticListPopulation(Collections.emptyList(), 10, -0.1);
 }

 // Test nextGeneration method with empty population
 @Test
 public void testNextGenerationWithEmptyPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 population.nextGeneration();
 assertEquals(10, population.size());
 }

 // Test nextGeneration method with a population
 @Test
 public void testNextGenerationWithPopulation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new Chromosome[] { new Chromosome(), new Chromosome(), new Chromosome() }), 10, 0.9);
 population.nextGeneration();
 assertEquals(10, population.size());
 }

 // Test setElitismRate method with valid elitismRate
 @Test
 public void testSetElitismRateWithValidElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 population.setElitismRate(0.9);
 assertEquals(0.9, population.getElitismRate(), 0.001);
 }

 // Test setElitismRate method with invalid elitismRate
 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateWithInvalidElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
 population.setElitismRate(-0.1);
 }

 // Test nextGeneration method with a valid population and elitismRate
 @Test
 public void testNextGenerationWithValidPopulationAndElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.singletonList(new Chromosome()), 10, 0.9);
 population.nextGeneration();
 assertEquals(1, population.size());
 }

 // Test nextGeneration method with a population larger than populationLimit
 @Test(expected = OutOfRangeException.class)
 public void testNextGenerationWithPopulationLargerThanPopulationLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new Chromosome[15]), 10, 0.9);
 population.nextGeneration();
 fail("Expected an OutOfRangeException but it didn't occur.");
 }

 // Test nextGeneration method with a population smaller than populationLimit
 @Test
 public void testNextGenerationWithPopulationSmallerThanPopulationLimit() {
 ElitisticListPopulation population = new ElitisticListPopulation(Arrays.asList(new Chromosome[5]), 10, 0.9);
 population.nextGeneration();
 assertEquals(10, population.size());
 }

}