import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        // Test construction with positive population limit
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        
        // Test construction with negative population limit
        try {
            ListPopulation listPopulationNegative = new ListPopulation(-10);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddChromosome() {
        // Test adding a chromosome within population limit
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        
        // Test adding a chromosome exceeding population limit
        for (int i = 0; i < 10; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        try {
            listPopulation.addChromosome(new Chromosome());
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetFittestChromosome() {
        // Test getting fittest chromosome from a list
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome bestChromosome = new Chromosome();
        bestChromosome.setFitness(10);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(bestChromosome);
        
        assertEquals(bestChromosome, listPopulation.getFittestChromosome());
    }
    
    // Add more test cases for all public methods as needed
    
}