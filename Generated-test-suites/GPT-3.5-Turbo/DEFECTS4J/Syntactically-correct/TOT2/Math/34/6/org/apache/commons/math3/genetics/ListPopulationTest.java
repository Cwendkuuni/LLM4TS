import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {
    
    private ListPopulation listPopulation;
    
    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test
    public void testAddChromosomes() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome1.setFitness(10);
        chromosome2.setFitness(20);
        
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationLimit() {
        new ListPopulation(-5);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            chromosomes.add(new Chromosome());
        }
        new ListPopulation(chromosomes, 5);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testExceedingPopulationLimit_AddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(chromosomes);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testExceedingPopulationLimit_AddChromosome() {
        for(int i = 0; i < 6; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation listPopulation2 = new ListPopulation(5);
        for(int i = 0; i < 3; i++) {
            listPopulation2.addChromosome(new Chromosome());
        }
        listPopulation2.setPopulationLimit(2);
    }
    
}