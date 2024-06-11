import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(new int[]{1, 0, 1, 0});
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(new int[]{1, 0, 1, 0});
        Chromosome chromosome2 = new Chromosome(new int[]{0, 1, 1, 0});
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome(new int[]{1, 0, 1, 0});
        Chromosome chromosome2 = new Chromosome(new int[]{0, 1, 1, 0});
        listPopulation.addChromosomes(List.of(chromosome1, chromosome2));
        assertEquals(2, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);
        listPopulation.setPopulationLimit(4);
        assertEquals(4, listPopulation.getPopulationLimit());
    }

    // Add more test cases for other methods as needed

}