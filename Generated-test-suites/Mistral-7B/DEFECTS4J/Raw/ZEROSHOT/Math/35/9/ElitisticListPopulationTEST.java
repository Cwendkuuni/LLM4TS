import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {

 private List<Chromosome> createChromosomesList(int size) {
 return new LinkedList<Chromosome>(Collections.nCopies(size, new DummyChromosome()));
 }

 @Test
 public void testElitisticListPopulationConstructorWithListAndLimit() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 15, 0.5);

 assertEquals(15, pop.size());
 assertEquals(chromosomes, pop.getChromosomes());
 }

 @Test
 public void testElitisticListPopulationConstructorWithLimitAndElitismRate() {
 ElitisticListPopulation pop = new ElitisticListPopulation(15, 0.5);

 assertEquals(15, pop.size());
 }

 @Test
 public void testNextGeneration() {
 List<Chromosome> chromosomes = createChromosomesList(10);
 ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 15, 0.5);

 Population nextGeneration = pop.nextGeneration();

 assertEquals(15, nextGeneration.size());
 }

 @Test
 public void testSetElitismRate() {
 ElitisticListPopulation pop = new ElitisticListPopulation(15, 0.5);

 pop.setElitismRate(0.7);

 assertEquals(0.7, pop.getElitismRate(), 0.001);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateOutOfBounds() {
 ElitisticListPopulation pop = new ElitisticListPopulation(15, 0.5);

 pop.setElitismRate(-0.1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetElitismRateAboveOne() {
 ElitisticListPopulation pop = new ElitisticListPopulation(15, 0.5);

 pop.setElitismRate(1.1);
 }

 @Test
 public void testGetElitismRate() {
 ElitisticListPopulation pop = new ElitisticListPopulation(15, 0.5);

 assertEquals(0.5, pop.getElitismRate(), 0.001);
 }

}

 class DummyChromosome implements Chromosome {
 @Override
 public double[] getGenotype() {
 return new double[0];
 }

 @Override
 public int getSize() {
 return 0;
 }

 @Override
 public void evolve() {
 // empty
 }
 }