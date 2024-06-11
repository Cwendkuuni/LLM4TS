import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getChromosomes().size());
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(new ArrayList<>());
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome(new ArrayList<>());
        Chromosome chromosome2 = new Chromosome(new ArrayList<>());
        chromosome1.setFitness(10);
        chromosome2.setFitness(5);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

    // Additional test cases for edge cases, exceptions, etc. can be added here

}