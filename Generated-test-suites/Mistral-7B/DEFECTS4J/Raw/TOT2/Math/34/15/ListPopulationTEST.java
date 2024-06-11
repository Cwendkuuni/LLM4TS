import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest1 {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        // Test basic functionality of getFittestChromosome()
        Chromosome chromosome1 = new Chromosome();
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new Chromosome();
        population.addChromosome(chromosome2);
        Chromosome fittest = population.getFittestChromosome();
        assertNotNull(fittest);
        // Test setPopulationLimit with a bigger value
        population.setPopulationLimit(10);
        assertEquals(5, population.getPopulationSize());
        // Test setPopulationLimit with a smaller value
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < ListPopulation.MAX_POPULATION_SIZE + 1; i++) {
            chromosomes.add(new Chromosome());
        }
        new ListPopulation(chromosomes, 5);
    }
}