import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(2);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome c1 = new Chromosome();
        c1.setFitness(10);
        Chromosome c2 = new Chromosome();
        c2.setFitness(5);
        population.addChromosome(c1);
        population.addChromosome(c2);
        assertEquals(c1, population.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(1);
    }

}