import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    ListPopulation listPopulation;

    @Test
    public void testConstructor_withEmptyList() {
        listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testConstructor_withChromosomesList() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation = new ListPopulation(chromosomes, 5);
        assertNotNull(listPopulation);
        assertEquals(2, listPopulation.getPopulationSize());
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation = new ListPopulation(5);
        listPopulation.setChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation = new ListPopulation(3);
        listPopulation.addChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();

        listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(chromosome);

        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);

        listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation = new ListPopulation(3);
        listPopulation.setPopulationLimit(5);

        assertEquals(5, listPopulation.getPopulationLimit());
    }

}