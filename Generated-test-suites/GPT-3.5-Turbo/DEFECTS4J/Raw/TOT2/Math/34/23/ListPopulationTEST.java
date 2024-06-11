import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        assertEquals(1, listPopulation.getPopulationSize());
        listPopulation.setChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(15);
        assertEquals(15, listPopulation.getPopulationLimit());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        assertEquals(1, listPopulation.getPopulationSize());
    }
}