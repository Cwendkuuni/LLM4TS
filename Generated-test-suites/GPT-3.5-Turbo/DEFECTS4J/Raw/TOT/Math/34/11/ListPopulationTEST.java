import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {
    
    private ListPopulation listPopulation;
    private List<Chromosome> chromosomes;
    
    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
    }
    
    @Test
    public void testConstructor_withValidPopulationLimit() {
        listPopulation = new ListPopulation(chromosomes, 100);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(100, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_withZeroPopulationLimit() {
        listPopulation = new ListPopulation(chromosomes, 0);
    }
    
    @Test
    public void testConstructor_withEmptyChromosomeList() {
        listPopulation = new ListPopulation(chromosomes, 100);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(100, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }
    
    @Test
    public void testConstructor_withPopulationLimitLessThanChromosomesListSize() {
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 0);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getPopulationLimit());
        assertEquals(1, listPopulation.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withPopulationLimitGreaterThanChromosomesListSize() {
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 0);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructor_withNullChromosomeList() {
        listPopulation = new ListPopulation(null, 100);
    }
    
    @Test
    public void testSetChromosomes_withValidChromosomeList() {
        chromosomes.add(new Chromosome());
        assertNotEquals(chromosomes, listPopulation.getChromosomes());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_withChromosomeListExceedsPopulationLimit() {
        chromosomes.add(new Chromosome());
        listPopulation.setPopulationLimit(0);
        listPopulation.setChromosomes(chromosomes);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_withNullChromosomeList() {
        listPopulation.setChromosomes(null);
    }
    
    @Test
    public void testAddChromosomes_withValidChromosomeCollection() {
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome());
        listPopulation.addChromosomes(chromosomesToAdd);
        assertEquals(chromosomesToAdd.size(), listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().containsAll(chromosomesToAdd));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_withChromosomeCollectionExceedsPopulationLimit() {
        listPopulation.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test
    public void testGetChromosomeList() {
        assertEquals(chromosomes, listPopulation.getChromosomeList());
    }
    
    @Test
    public void testAddChromosome_withValidChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_withExceedingPopulationLimit() {
        listPopulation.setPopulationLimit(0);
        listPopulation.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome_withValidChromosomes() {
        Chromosome chromosome1 = new Chromosome(1); // Fitness = 1
        Chromosome chromosome2 = new Chromosome(2); // Fitness = 2
        Chromosome chromosome3 = new Chromosome(3); // Fitness = 3
        
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        listPopulation.addChromosomes(chromosomes);
        
        assertEquals(chromosome3, listPopulation.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(100, listPopulation.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_withZeroPopulationLimit() {
        listPopulation.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_withSmallerPopulationLimit() {
        listPopulation.setPopulationLimit(10);
    }
    
    @Test
    public void testSetPopulationLimit_withValidPopulationLimit() {
        listPopulation.setPopulationLimit(200);
        assertEquals(200, listPopulation.getPopulationLimit());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(listPopulation.iterator());
    }
}