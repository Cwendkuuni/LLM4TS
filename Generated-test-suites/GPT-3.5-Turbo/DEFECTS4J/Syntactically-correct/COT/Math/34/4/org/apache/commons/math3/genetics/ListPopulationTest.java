import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    // Test case for ListPopulation constructor with population limit
    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    // Test case for ListPopulation constructor with list of chromosomes and population limit
    @Test
    public void testListPopulationConstructorWithChromosomes() {
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        assertNotNull(listPopulation);
        assertEquals(2, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    // Test case for setChromosomes method
    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    // Test case for addChromosomes method
    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    // Test case for getChromosomes method
    @Test
    public void testGetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getChromosomes().size());
    }

    // Test case for getChromosomeList method
    @Test
    public void testGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getChromosomeList().size());
    }

    // Test case for addChromosome method
    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    // Test case for getFittestChromosome method
    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        assertEquals(chromosome3, listPopulation.getFittestChromosome());
    }

    // Test case for getPopulationLimit method
    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    // Test case for setPopulationLimit method
    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    // Test case for getPopulationSize method
    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        String expected = "[" + chromosome.toString() + "]";
        assertEquals(expected, listPopulation.toString());
    }

    // Test case for iterator method
    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(10);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertNotNull(iterator);
    }
}