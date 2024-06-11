import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 5);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.addChromosomes(chromosomes);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome(5);
        Chromosome chromosome2 = new Chromosome(10);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome(5);
        Chromosome chromosome2 = new Chromosome(10);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals("[Chromosome: 5, Chromosome: 10]", listPopulation.toString());
    }

}