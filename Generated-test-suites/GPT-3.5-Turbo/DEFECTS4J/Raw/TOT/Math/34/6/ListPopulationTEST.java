import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    // Test case for the constructor ListPopulation(int)
    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    // Test case for the constructor ListPopulation(List<Chromosome>, int)
    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
    }

    // Test case for the constructor ListPopulation(List<Chromosome>, int) with invalid input
    @Test(expected = NullPointerException.class)
    public void testConstructorWithInvalidChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    // Test case for the constructor ListPopulation(List<Chromosome>, int) with invalid population limit
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, -5);
    }

    // Test case for setChromosomes(List<Chromosome>)
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
    }

    // Test case for setChromosomes(List<Chromosome>) with null chromosomes
    @Test(expected = NullPointerException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(null);
    }

    // Test case for setChromosomes(List<Chromosome>) with invalid chromosomes
    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesWithInvalidChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(1);
        population.setChromosomes(chromosomes);
    }

    // Test case for addChromosomes(Collection<Chromosome>)
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(3);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(1, population.getPopulationSize());
    }

    // Test case for addChromosomes(Collection<Chromosome>) with too many chromosomes
    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(1);
        population.addChromosomes(chromosomes);
    }

    // Test case for getChromosomes()
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for getFittestChromosome()
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(0.5);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.8);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(chromosome2, population.getFittestChromosome());
    }

    // Test case for getPopulationSize()
    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(2, population.getPopulationSize());
    }

}