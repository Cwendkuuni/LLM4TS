import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    
    // Test data
    private List<Chromosome> chromosomes;
    private int populationLimit;
    
    @Before
    public void setup() {
        // Initialize test data
        chromosomes = new ArrayList<>();
        populationLimit = 5;
    }
    
    // Test case for constructor ListPopulation(int)
    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }
    
    // Test case for constructor ListPopulation(List, int)
    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(2, listPopulation.getPopulationSize());
    }
    
    // Test case for constructor ListPopulation(List, int) with null chromosome list
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, populationLimit);
    }
    
    // Test case for constructor ListPopulation(List, int) with negative population limit
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(chromosomes, -1);
    }
    
    // Test case for constructor ListPopulation(List, int) with chromosome list size exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesBiggerThanPopulationSize() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        new ListPopulation(chromosomes, 2);
    }
    
    // Test case for addChromosomes(Collection)
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomeColl = Arrays.asList(new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.addChromosomes(chromosomeColl);
        assertEquals(2, listPopulation.getPopulationSize());
    }
    
    // Test case for addChromosomes(Collection) with collection size exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomeColl = Arrays.asList(new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.addChromosomes(Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome()));
    }
    
    // Test case for setChromosomes(List)
    @Test
    public void testSetChromosomes() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }
    
    // Test case for setChromosomes(List) with null chromosome list
    @Test(expected = NullArgumentException.class)
    public void testSetNullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(null);
    }
    
    // Test case for setChromosomes(List) with chromosome list size exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(chromosomes);
    }
    
    // Test case for addChromosome(Chromosome)
    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }
    
    // Test case for addChromosome(Chromosome) with population size reaching population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeReachPopulationLimit() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(chromosomes);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
    }
    
    // Test case for getFittestChromosome()
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }
    
    // Test case for getPopulationSize()
    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        assertEquals(2, listPopulation.getPopulationSize());
    }
    
    // Test case for getPopulationLimit()
    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }
    
    // Test case for setPopulationLimit(int)
    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        int newPopulationLimit = 10;
        listPopulation.setPopulationLimit(newPopulationLimit);
        assertEquals(newPopulationLimit, listPopulation.getPopulationLimit());
    }
    
    // Test case for setPopulationLimit(int) with negative population limit
    @Test(expected = NotPositiveException.class)
    public void testSetNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setPopulationLimit(-1);
    }
    
    // Test case for setPopulationLimit(int) with new population limit smaller than current population size
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        listPopulation.setPopulationLimit(1);
    }
    
    // Test case for iterator()
    @Test
    public void testIterator() {
        chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        int count = 0;
        for (Chromosome chromosome : listPopulation) {
            count++;
        }
        assertEquals(chromosomes.size(), count);
    }
    
    // Additional test cases can be added to cover more scenarios

}