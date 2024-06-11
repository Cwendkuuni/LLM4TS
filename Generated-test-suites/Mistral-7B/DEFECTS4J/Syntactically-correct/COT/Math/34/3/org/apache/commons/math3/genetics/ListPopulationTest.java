import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationSetterWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetterWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationAddChromosomeWithPopulationLimitNotExceeded() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        for (int i = 0; i < 7; i++) {
            population.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testListPopulationAddChromosomesWithPopulationLimitNotExceeded() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
        assertEquals(10, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new Chromosome());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 2);
        population.addChromosome(new Chromosome(1.0f, 1.0f));
        population.addChromosome(new Chromosome(0.5f, 0.5f));
        Chromosome expected = population.getChromosomes().get(0);
        Chromosome actual = population.getFittestChromosome();
        assertEquals(expected, actual);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 2);
        population.addChromosome(new Chromosome(1.0f, 1.0f));
        population.addChromosome(new Chromosome(0.5f, 0.5f));
        String expected = "[Chromosome[fitness=1.0, genotype=[1.0, 1.0]], Chromosome[fitness=0.5, genotype=[0.5, 0.5]]]";
        String actual = population.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 2);
        population.addChromosome(new Chromosome(1.0f, 1.0f));
        population.addChromosome(new Chromosome(0.5f, 0.5f));
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome expected = population.getChromosomes().get(0);
        Chromosome actual = iterator.next();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }
}