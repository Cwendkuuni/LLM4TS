import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor_WithValidLimit_ShouldCreateListPopulationObject() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
    }

    @Test
    public void testListPopulationAddChromosome_WithValidChromosome_ShouldAddChromosomeToPopulation() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testListPopulationGetFittestChromosome_WithMultipleChromosomes_ShouldReturnFittestChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    // Add more test cases for other methods as needed

}