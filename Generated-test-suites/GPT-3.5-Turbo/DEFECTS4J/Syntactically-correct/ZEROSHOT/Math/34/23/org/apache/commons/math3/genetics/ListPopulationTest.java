import org.hamcrest.Matchers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(1, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, -10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setPopulationLimit(1);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(1);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(1);
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertSame(chromosomes, populationChromosomes);
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        List<Chromosome> populationChromosomes = population.getChromosomeList();
        assertSame(chromosomes, populationChromosomes);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(30);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertSame(chromosome3, fittestChromosome);
        assertEquals(30, fittestChromosome.getFitness());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        Chromosome chromosome = iterator.next();
        assertSame(chromosomes.get(0), chromosome);
        assertFalse(iterator.hasNext());
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertThat(ex.getMessage(), Matchers.containsString("remove"));
        }
    }

}