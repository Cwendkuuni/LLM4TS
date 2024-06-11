import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import static org.junit.Assert.*;

public class ListPopulationTest {

	@Test
	public void testListPopulationEmptyConstructor() {
		ListPopulation population = new ListPopulation(10);
		
		assertEquals(10, population.getPopulationLimit());
		assertEquals(0, population.getPopulationSize());
	}
	
	@Test
	public void testListPopulationConstructorWithPopulation() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
		
		assertEquals(10, population.getPopulationLimit());
		assertEquals(2, population.getPopulationSize());
		assertTrue(population.getChromosomes().contains(chromosome1));
		assertTrue(population.getChromosomes().contains(chromosome2));
	}
	
	@Test(expected = NotPositiveException.class)
	public void testListPopulationConstructorWithNegativePopulationLimit() {
		ListPopulation population = new ListPopulation(-10);
	}
	
	@Test(expected = NotPositiveException.class)
	public void testSetPopulationLimitWithNegativePopulationLimit() {
		ListPopulation population = new ListPopulation(10);
		population.setPopulationLimit(-5);
	}
	
	@Test(expected = NumberIsTooSmallException.class)
	public void testSetPopulationLimitWithSmallerLimitThanPopulationSize() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
		population.setPopulationLimit(1);
	}
	
	@Test
	public void testAddChromosome() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome = new Chromosome();
		population.addChromosome(chromosome);
		
		assertEquals(10, population.getPopulationLimit());
		assertEquals(1, population.getPopulationSize());
		assertTrue(population.getChromosomes().contains(chromosome));
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosomeExceedPopulationLimit() {
		ListPopulation population = new ListPopulation(1);
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
	}
	
	@Test
	public void testGetFittestChromosome() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome1 = new Chromosome();
		chromosome1.setFitness(10);
		Chromosome chromosome2 = new Chromosome();
		chromosome2.setFitness(5);
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
		
		assertEquals(chromosome1, population.getFittestChromosome());
	}
	
	@Test
	public void testIterator() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
		
		int count = 0;
		for (Chromosome chromosome : population) {
			count++;
		}
		
		assertEquals(2, count);
	}
	
	@Test
	public void testToString() {
		ListPopulation population = new ListPopulation(10);
		Chromosome chromosome1 = new Chromosome();
		Chromosome chromosome2 = new Chromosome();
		population.addChromosome(chromosome1);
		population.addChromosome(chromosome2);
		
		assertEquals("[chromosome1, chromosome2]", population.toString());
	}
}