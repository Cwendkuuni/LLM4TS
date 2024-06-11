import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testPopulationLimitConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testChromosomesPopulationLimitConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 5);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.addChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
        assertThrows(NumberIsTooLargeException.class, () -> listPopulation.addChromosome(new Chromosome()));
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome c1 = new Chromosome();
        c1.setFitness(10.0);
        Chromosome c2 = new Chromosome();
        c2.setFitness(5.0);
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(c1, c2), 5);
        assertEquals(c1, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitSmallerList() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome(), new Chromosome()), 3);
        assertThrows(NumberIsTooSmallException.class, () -> listPopulation.setPopulationLimit(1));
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(3);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertTrue(iterator.hasNext());
    }
}