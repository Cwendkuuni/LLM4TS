import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.setChromosomes(new ArrayList<>());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getPopulationSize());
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(2);
        assertEquals(0, listPopulation.getPopulationSize());
        Chromosome chromosome1 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(20);
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceedPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetSmallPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals("[]", listPopulation.toString());
    }
}