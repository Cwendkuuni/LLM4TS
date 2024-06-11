import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructorWithPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);

        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testConstructorWithPopulaionLimitLowerThan1() {
        try {
            int populationLimit = -1;
            ListPopulation population = new ListPopulation(populationLimit);
        } catch (NotPositiveException e) {
            assertEquals("Population limit is not a positive number (< 1)", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        int populationLimit = 10;

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testConstructorWithNullChromosomes() {
        try {
            int populationLimit = 10;
            List<Chromosome> chromosomes = null;
            ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        } catch (NullArgumentException e) {
            assertEquals("List of chromosomes is null", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        try {
            Chromosome chromosome1 = new Chromosome();
            Chromosome chromosome2 = new Chromosome();
            int populationLimit = 1;

            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(chromosome1);
            chromosomes.add(chromosome2);

            ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        } catch (NumberIsTooLargeException e) {
            assertEquals("List of chromosomes exceeds the population limit", e.getMessage());
            assertEquals(2, e.getActual());
            assertEquals(1, e.getExpected());
        }
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testSetChromosomesWithNullChromosomes() {
        try {
            ListPopulation population = new ListPopulation(10);
            List<Chromosome> chromosomes = null;
            population.setChromosomes(chromosomes);
        } catch (NullArgumentException e) {
            assertEquals("List of chromosomes is null", e.getMessage());
        }
    }

    @Test
    public void testSetChromosomesExceedingPopulationLimit() {
        try {
            Chromosome chromosome1 = new Chromosome();
            Chromosome chromosome2 = new Chromosome();

            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(chromosome1);
            chromosomes.add(chromosome2);

            ListPopulation population = new ListPopulation(1);
            population.setChromosomes(chromosomes);
        } catch (NumberIsTooLargeException e) {
            assertEquals("List of chromosomes exceeds the population limit", e.getMessage());
            assertEquals(2, e.getActual());
            assertEquals(1, e.getExpected());
        }
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(chromosomes);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testAddChromosomesExceedingPopulationLimit() {
        try {
            Chromosome chromosome1 = new Chromosome();
            Chromosome chromosome2 = new Chromosome();

            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(chromosome1);
            chromosomes.add(chromosome2);

            ListPopulation population = new ListPopulation(1);
            population.addChromosomes(chromosomes);
        } catch (NumberIsTooLargeException e) {
            assertEquals("List of chromosomes exceeds the population limit", e.getMessage());
            assertEquals(2, e.getActual());
            assertEquals(1, e.getExpected());
        }
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.isEmpty());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertTrue(chromosomes.isEmpty());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();

        ListPopulation population = new ListPopulation(10);
        population.addChromosome(chromosome);

        assertEquals(chromosome, population.getChromosomes().get(0));
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testAddChromosomeExceedingPopulationLimit() {
        try {
            Chromosome chromosome1 = new Chromosome();
            Chromosome chromosome2 = new Chromosome();

            ListPopulation population = new ListPopulation(1);
            population.addChromosome(chromosome1);
            population.addChromosome(chromosome2);
        } catch (NumberIsTooLargeException e) {
            assertEquals("List of chromosomes exceeds the population limit", e.getMessage());
            assertEquals(2, e.getActual());
            assertEquals(1, e.getExpected());
        }
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(5.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(10.0);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(7.5);

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittest = population.getFittestChromosome();

        assertEquals(chromosome2, fittest);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);

        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        int populationLimit = 20;
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(populationLimit);

        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithLowerNumber() {
        try {
            int populationLimit = 5;
            ListPopulation population = new ListPopulation(10);
            population.setPopulationLimit(populationLimit);
        } catch (NumberIsTooSmallException e) {
            assertEquals("New population size is smaller than current number of chromosomes", e.getMessage());
        }
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());

        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

}