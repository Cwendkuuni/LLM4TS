import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        // Test constructor with positive population limit
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        
        // Test constructor with non-positive population limit, should throw NotPositiveException
        try {
            ListPopulation listPopulationInvalid = new ListPopulation(0);
            fail("NotPositiveException expected");
        } catch (NotPositiveException e) {
            // success
        }
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(); // Assume Chromosome class is defined
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        
        // Test adding chromosomes exceeding population limit, should throw NumberIsTooLargeException
        try {
            for (int i = 0; i < 11; i++) {
                listPopulation.addChromosome(new Chromosome());
            }
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // success
        }
    }

    // Add more test cases for other public methods of ListPopulation class

}