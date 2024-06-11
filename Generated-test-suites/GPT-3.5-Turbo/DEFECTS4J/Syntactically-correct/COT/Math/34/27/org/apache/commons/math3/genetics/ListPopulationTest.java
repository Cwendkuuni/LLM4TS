import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulation() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertNotNull(population);
        assertEquals(2, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithZeroPopulationLimit() {
        ListPopulation population = new ListPopulation(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithTooLargeChromosomesList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesWithTooLargeChromosomesList() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomesWithTooLargeChromosomesList() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomeWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = new Chromosome();
        assertTrue(fittestChromosome.compareTo(population.getChromosomes().get(0)) > 0);
        population.addChromosome(fittestChromosome);
        assertEquals(fittestChromosome, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPopulationLimitWithTooSmallPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

}