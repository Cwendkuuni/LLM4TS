import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        int populationLimit = 100;
        ListPopulation population = new ListPopulation(populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorInvalidPopulationLimit() {
        int populationLimit = -10;
        ListPopulation population = new ListPopulation(populationLimit);
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        int populationLimit = 100;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        int populationLimit = 100;
        List<Chromosome> chromosomes = null;
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithInvalidPopulationLimit() {
        int populationLimit = -10;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithChromosomesExceedingLimit() {
        int populationLimit = 100;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        for (int i = 0; i < populationLimit + 1; i++) {
            chromosomes.add(new Chromosome());
        }
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = null;
        population.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        for (int i = 0; i < population.getPopulationLimit() + 1; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        for (int i = 0; i < population.getPopulationLimit() + 1; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(100);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingLimit() {
        ListPopulation population = new ListPopulation(100);
        for (int i = 0; i < population.getPopulationLimit(); i++) {
            population.addChromosome(new Chromosome());
        }
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(100);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 100;
        ListPopulation population = new ListPopulation(populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        int populationLimit = -10;
        population.setPopulationLimit(populationLimit);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        for (int i = 0; i < 100; i++) {
            chromosomes.add(new Chromosome());
        }
        ListPopulation population = new ListPopulation(chromosomes, 100);
        int populationLimit = 50;
        population.setPopulationLimit(populationLimit);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        int populationLimit = 200;
        population.setPopulationLimit(populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        for (Chromosome chromosome : population) {
            assertNotNull(chromosome);
        }
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(100);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertNotNull(population.toString());
    }

}