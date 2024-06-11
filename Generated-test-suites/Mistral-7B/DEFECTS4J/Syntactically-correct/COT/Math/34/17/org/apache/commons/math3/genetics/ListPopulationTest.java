import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(new float[10]);
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(new float[10]));
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(new float[10]);
        population.addChromosome(chromosome);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
        assertEquals(chromosome, chromosomes.get(0));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome bestChromosome = new Chromosome(new float[10]);
        population.addChromosome(bestChromosome);
        population.addChromosome(new Chromosome(new float[10]));
        Chromosome worstChromosome = new Chromosome(new float[10]);
        population.addChromosome(worstChromosome);
        assertEquals(bestChromosome, population.getFittestChromosome());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(new float[10]));
        chromosomes.add(new Chromosome(new float[10]));
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new Chromosome(new float[10]));
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(new float[10]);
        population.addChromosome(chromosome);
        assertEquals("[Chromosome[fitness=0.0, genotype=...], Chromosome[fitness=0.0, genotype=...]]",
                population.toString());
    }
}