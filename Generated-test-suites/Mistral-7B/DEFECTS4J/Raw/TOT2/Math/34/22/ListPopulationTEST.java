import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithPositiveLimit() {
        ListPopulation population = new ListPopulation(5); // creating a population with a limit of 5
        Chromosome chromosome = new DummyChromosome(); // assuming a DummyChromosome class exists
        population.addChromosome(chromosome);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
        population.addChromosome(chromosome); // adding the same chromosome again
        assertEquals(2, chromosomes.size());
        population.addChromosomes(Collections.singletonList(new DummyChromosome())); // adding a single chromosome
        assertEquals(3, chromosomes.size());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativeLimit() {
        ListPopulation population = new ListPopulation(-3); // creating a population with a negative limit
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5, null); // creating a population with a null list of chromosomes
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 1; i <= ListPopulation.MAX_POPULATION_SIZE + 1; i++) {
            chromosomes.add(new DummyChromosome());
        }
        ListPopulation population = new ListPopulation(chromosomes, 5); // creating a population with too many chromosomes
    }

    @Test
    public void testListPopulationWithSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            chromosomes.add(new DummyChromosome());
        }
        ListPopulation population = new ListPopulation(population.getChromosomes()); // creating a population with existing chromosomes
        assertEquals(5, population.getPopulationSize());
        population.setChromosomes(Collections.singletonList(new DummyChromosome())); // setting the population with a single chromosome
        assertEquals(1, population.getPopulationSize());
    }
}