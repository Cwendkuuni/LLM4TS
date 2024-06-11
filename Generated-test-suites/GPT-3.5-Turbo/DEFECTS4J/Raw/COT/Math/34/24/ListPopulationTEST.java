import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getChromosomes().size());
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testAddChromosomes_Exception() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        for (int i = 0; i < 10; i++) {
            listPopulation.addChromosome(chromosome);
        }
        try {
            listPopulation.addChromosome(chromosome);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // exception successfully thrown
        }
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testAddChromosome_Exception() {
        ListPopulation listPopulation = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        try {
            listPopulation.addChromosome(chromosome2);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // exception successfully thrown
        }
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit_Exception() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        try {
            listPopulation.setPopulationLimit(1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // exception successfully thrown
        }
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}