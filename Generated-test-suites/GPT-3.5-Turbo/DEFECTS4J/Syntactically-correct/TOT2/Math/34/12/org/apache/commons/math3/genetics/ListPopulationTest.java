import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructor_EmptyList() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testConstructor_WithChromosomes() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(), 10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosomes(List.of(chromosome2));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.setChromosomes(List.of(chromosome2));
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5.0);
        listPopulation.addChromosomes(List.of(chromosome1, chromosome2));
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
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
            // expected
        }
    }

    @Test
    public void testSetPopulationLimit_Exception() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosome(new Chromosome());
        try {
            listPopulation.setPopulationLimit(1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        int count = 0;
        for (Chromosome c : listPopulation) {
            assertEquals(chromosome, c);
            count++;
        }
        assertEquals(1, count);
    }
}