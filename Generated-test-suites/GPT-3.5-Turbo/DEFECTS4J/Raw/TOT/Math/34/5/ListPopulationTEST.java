import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;


public class ListPopulationTest {

	private ListPopulation listPopulation;
	private List<Chromosome> chromosomes;
	private int populationLimit;
	
	@Before
	public void setUp() {
		populationLimit = 10;
		chromosomes = new ArrayList<Chromosome>();
		listPopulation = new ListPopulation(chromosomes, populationLimit);
	}
	
	@Test
	public void testConstructor_WithValidChromosomes_ValidPopulationLimit_ShouldInitializeListPopulation() {
		// Arrange
		chromosomes.add(new Chromosome());
		
		// Act
		ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
		
		// Assert
		assertEquals(chromosomes, listPopulation.getChromosomes());
		assertEquals(populationLimit, listPopulation.getPopulationLimit());
	}
	
	@Test(expected = NullArgumentException.class)
	public void testConstructor_WithNullChromosomes_ShouldThrowNullArgumentException() {
		// Act
		ListPopulation listPopulation = new ListPopulation(null, populationLimit);
	}
	
	@Test(expected = NotPositiveException.class)
	public void testConstructor_WithInvalidPopulationLimit_ShouldThrowNotPositiveException() {
		// Act
		ListPopulation listPopulation = new ListPopulation(chromosomes, -1);
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testConstructor_WithChromosomesExceedingPopulationLimit_ShouldThrowNumberIsTooLargeException() {
		// Arrange
		chromosomes.add(new Chromosome());
		chromosomes.add(new Chromosome());
		
		// Act
		ListPopulation listPopulation = new ListPopulation(chromosomes, 1);
	}
	
	@Test
	public void testSetChromosomes_WithValidChromosomes_ShouldUpdateChromosomes() {
		// Arrange
		List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		newChromosomes.add(new Chromosome());
		
		// Act
		listPopulation.setChromosomes(newChromosomes);
		
		// Assert
		assertEquals(newChromosomes, listPopulation.getChromosomes());
	}
	
	@Test(expected = NullArgumentException.class)
	public void testSetChromosomes_WithNullChromosomes_ShouldThrowNullArgumentException() {
		// Act
		listPopulation.setChromosomes(null);
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testSetChromosomes_WithChromosomesExceedingPopulationLimit_ShouldThrowNumberIsTooLargeException() {
		// Arrange
		List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		newChromosomes.add(new Chromosome());
		newChromosomes.add(new Chromosome());
		
		// Act
		listPopulation.setChromosomes(newChromosomes);
	}
	
	@Test
	public void testAddChromosomes_WithValidChromosomeCollection_ShouldAddChromosomesToPopulation() {
		// Arrange
		List<Chromosome> chromosomeColl = new ArrayList<Chromosome>();
		chromosomeColl.add(new Chromosome());
		
		// Act
		listPopulation.addChromosomes(chromosomeColl);
		
		// Assert
		assertTrue(listPopulation.getChromosomes().containsAll(chromosomeColl));
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosomes_WithChromosomesExceedingPopulationLimit_ShouldThrowNumberIsTooLargeException() {
		// Arrange
		List<Chromosome> chromosomeColl = new ArrayList<Chromosome>();
		chromosomeColl.add(new Chromosome());
		chromosomeColl.add(new Chromosome());
		
		// Act
		listPopulation.addChromosomes(chromosomeColl);
	}
	
	@Test
	public void testGetChromosomes_ShouldReturnUnmodifiableList() {
		// Arrange
		chromosomes.add(new Chromosome());
		
		// Act
		List<Chromosome> result = listPopulation.getChromosomes();
		
		// Assert
		assertTrue(result instanceof java.util.Collections.UnmodifiableList);
		assertEquals(chromosomes, result);
	}
	
	@Test
	public void testAddChromosome_WithValidChromosome_ShouldAddChromosomeToPopulation() {
		// Arrange
		Chromosome chromosome = new Chromosome();
		
		// Act
		listPopulation.addChromosome(chromosome);
		
		// Assert
		assertTrue(listPopulation.getChromosomes().contains(chromosome));
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosome_WithPopulationLimitReached_ShouldThrowNumberIsTooLargeException() {
		// Arrange
		for (int i = 0; i < populationLimit; i++) {
			listPopulation.addChromosome(new Chromosome());
		}
		
		// Act
		listPopulation.addChromosome(new Chromosome());
	}
	
	@Test
	public void testGetFittestChromosome_WithChromosomesInPopulation_ShouldReturnFittestChromosome() {
		// Arrange
		Chromosome expected = new Chromosome();
		chromosomes.add(new Chromosome());
		chromosomes.add(expected);
		
		// Act
		Chromosome result = listPopulation.getFittestChromosome();
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void testGetPopulationLimit_ShouldReturnPopulationLimit() {
		// Arrange
		
		// Act
		int result = listPopulation.getPopulationLimit();
		
		// Assert
		assertEquals(populationLimit, result);
	}
	
	@Test
	public void testSetPopulationLimit_WithValidPopulationLimit_ShouldUpdatePopulationLimit() {
		// Arrange
		int newPopulationLimit = 20;
		
		// Act
		listPopulation.setPopulationLimit(newPopulationLimit);
		
		// Assert
		assertEquals(newPopulationLimit, listPopulation.getPopulationLimit());
	}
	
	@Test(expected = NotPositiveException.class)
	public void testSetPopulationLimit_WithInvalidPopulationLimit_ShouldThrowNotPositiveException() {
		// Act
		listPopulation.setPopulationLimit(-1);
	}
	
	@Test(expected = NumberIsTooSmallException.class)
	public void testSetPopulationLimit_WithNewPopulationLimitSmallerThanCurrentPopulationSize_ShouldThrowNumberIsTooSmallException() {
		// Arrange
		int newPopulationLimit = 1;
		chromosomes.add(new Chromosome());
		
		// Act
		listPopulation.setPopulationLimit(newPopulationLimit);
	}
	
	@Test
	public void testGetPopulationSize_WithChromosomesInPopulation_ShouldReturnPopulationSize() {
		// Arrange
		chromosomes.add(new Chromosome());
		
		// Act
		int result = listPopulation.getPopulationSize();
		
		// Assert
		assertEquals(chromosomes.size(), result);
	}
	
	@Test
	public void testToString_WithChromosomesInPopulation_ShouldReturnStringRepresentation() {
		// Arrange
		chromosomes.add(new Chromosome());
		
		// Act
		String result = listPopulation.toString();
		
		// Assert
		assertEquals(chromosomes.toString(), result);
	}
	
	@Test
	public void testIterator_WithChromosomesInPopulation_ShouldReturnIteratorOverChromosomes() {
		// Arrange
		chromosomes.add(new Chromosome());
		
		// Act
		Iterator<Chromosome> iterator = listPopulation.iterator();
		
		// Assert
		assertTrue(iterator.hasNext());
		assertEquals(chromosomes.get(0), iterator.next());
		assertFalse(iterator.hasNext());
	}
}