import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidInput() {
        ListPopulation population = new ListPopulation(5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        ListPopulation populationWithChromosomes = new ListPopulation(chromosomes, 5);
        assertSame(chromosomes, populationWithChromosomes.getChromosomes());
        assertEquals(2, populationWithChromosomes.getPopulationSize());

        Chromosome chromosome = new Chromosome() {};
        populationWithChromosomes.addChromosome(chromosome);
        assertEquals(3, populationWithChromosomes.getPopulationSize());
        assertEquals(chromosome, populationWithChromosomes.getFittestChromosome());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testSetChromosomesDeprecatedMethod() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosomesMethod() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullListInputForSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesMethodWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullChromosomeInputForAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeMethodWithPopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new Chromosome() {});
        }
        population.addChromosomes(chromosomes);
    }
}