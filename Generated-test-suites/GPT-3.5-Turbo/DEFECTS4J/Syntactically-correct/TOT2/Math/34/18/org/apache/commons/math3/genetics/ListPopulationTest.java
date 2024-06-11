import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
    }
    
    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }
    
    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosomes(List.of(chromosome1, chromosome2));
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }
    
    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(5);
        Chromosome chromosome2 = new Chromosome(10);
        listPopulation.addChromosomes(List.of(chromosome1, chromosome2));
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }
    
    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.setChromosomes(List.of(chromosome1, chromosome2));
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }
    
    @Test
    public void testListPopulationToString() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation.toString());
    }

}