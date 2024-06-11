import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {
	
	private ListPopulation population;
	private List<Chromosome> chromosomes;
	private int populationLimit;
	
	@Before
	public void setup() {
		populationLimit = 10;
		chromosomes = new ArrayList<>();
		population = new ListPopulation(chromosomes, populationLimit);
	}
	
	@Test(expected = NotPositiveException.class)
	public void testConstructor_PopulationLimitInvalid() {
		new ListPopulation(-1); // population limit = -1
	}
	
	@Test
	public void testConstructor_PopulationLimitValid() {
		ListPopulation pop = new ListPopulation(10); // population limit = 10
		assertEquals(10, pop.getPopulationLimit());
	}
	
	@Test(expected = NullArgumentException.class)
	public void testConstructor_ChromosomesNull() {
		ListPopulation pop = new ListPopulation(null, 10); // chromosomes = null
	}
	
	@Test(expected = NotPositiveException.class)
	public void testConstructor_ChromosomesPopLimitInvalid() {
		List<Chromosome> invalidChromosomes = new ArrayList<>();
		invalidChromosomes.add(new Chromosome());
		ListPopulation pop = new ListPopulation(invalidChromosomes, -1); // population limit = -1
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testConstructor_ChromosomesExceedsPopLimit() {
		List<Chromosome> invalidChromosomes = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			invalidChromosomes.add(new Chromosome());
		}
		ListPopulation pop = new ListPopulation(invalidChromosomes, 10); // chromosomes size = 11, population limit = 10
	}
	
	@Test
	public void testConstructor_ChromosomesValid() {
		List<Chromosome> validChromosomes = new ArrayList<>();
		validChromosomes.add(new Chromosome());
		ListPopulation pop = new ListPopulation(validChromosomes, 10); // chromosomes size = 1, population limit = 10
		assertEquals(1, pop.getChromosomes().size());
	}
	
	@Test
	public void testSetChromosomes() {
		List<Chromosome> newChromosomes = new ArrayList<>();
		newChromosomes.add(new Chromosome());
		population.setChromosomes(newChromosomes);
		assertEquals(1, population.getChromosomes().size());
	}
	
	@Test(expected = NullArgumentException.class)
	public void testSetChromosomesNull() {
		population.setChromosomes(null); // chromosomes = null
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testSetChromosomesExceedsPopLimit() {
		List<Chromosome> invalidChromosomes = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			invalidChromosomes.add(new Chromosome());
		}
		population.setChromosomes(invalidChromosomes); // chromosomes size = 11, population limit = 10
	}
	
	@Test
	public void testAddChromosomes() {
		List<Chromosome> additionalChromosomes = new ArrayList<>();
		additionalChromosomes.add(new Chromosome());
		population.addChromosomes(additionalChromosomes);
		assertEquals(1, population.getChromosomes().size());
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosomesExceedsPopLimit() {
		List<Chromosome> invalidChromosomes = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			invalidChromosomes.add(new Chromosome());
		}
		population.addChromosomes(invalidChromosomes); // chromosomes size = 11, population limit = 10
	}
	
	@Test
	public void testGetChromosomes() {
		List<Chromosome> validChromosomes = new ArrayList<>();
		validChromosomes.add(new Chromosome());
		ListPopulation pop = new ListPopulation(validChromosomes, 10); // chromosomes size = 1, population limit = 10
		assertEquals(1, pop.getChromosomes().size());
	}
	
	@Test
	public void testGetChromosomeList() {
		assertEquals(chromosomes, population.getChromosomeList());
	}
	
	@Test
	public void testAddChromosome() {
		population.addChromosome(new Chromosome());
		assertEquals(1, population.getChromosomes().size());
	}
	
	@Test(expected = NumberIsTooLargeException.class)
	public void testAddChromosomeExceedsPopLimit() {
		for (int i = 0; i < 11; i++) {
			population.addChromosome(new Chromosome());
		}
	}
	
	@Test
	public void testGetFittestChromosome() {
		Chromosome c1 = new Chromosome(1);
		Chromosome c2 = new Chromosome(2);
		Chromosome c3 = new Chromosome(3);
		chromosomes.add(c1);
		chromosomes.add(c2);
		chromosomes.add(c3);
		Chromosome fittestChromosome = population.getFittestChromosome();
		assertEquals(c3, fittestChromosome);
	}
	
	@Test
	public void testGetPopulationLimit() {
		assertEquals(populationLimit, population.getPopulationLimit());
	}
	
	@Test(expected = NotPositiveException.class)
	public void testSetPopulationLimitInvalid() {
		population.setPopulationLimit(-1); // population limit = -1
	}
	
	@Test(expected = NumberIsTooSmallException.class)
	public void testSetPopulationLimitSmallerThanChromosomesSize() {
		population.setPopulationLimit(1); // population limit = 1, chromosomes size = 0
	}
	
	@Test
	public void testSetPopulationLimitValid() {
		population.setPopulationLimit(20); // population limit = 20
		assertEquals(20, population.getPopulationLimit());
	}
	
	@Test
	public void testGetPopulationSize() {
		assertEquals(0, population.getPopulationSize());
	}
	
	@Test
	public void testToString() {
		assertEquals(chromosomes.toString(), population.toString());
	}
	
	@Test
	public void testIterator() {
		assertEquals(chromosomes.iterator().next(), population.iterator().next());
	}
	
}