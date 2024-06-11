import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class ListPopulationTest {

    // Test case for ListPopulation constructor with a positive populationLimit
    @Test
    public void testListPopulationConstructor_PositivePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    // Test case for ListPopulation constructor with a negative populationLimit
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructor_NegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(-10);
    }

    // Test case for adding chromosomes to ListPopulation
    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    // Test case for adding too many chromosomes to ListPopulation
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ExceedPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
    }

    // Test case for getting fittest chromosome in ListPopulation
    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

}