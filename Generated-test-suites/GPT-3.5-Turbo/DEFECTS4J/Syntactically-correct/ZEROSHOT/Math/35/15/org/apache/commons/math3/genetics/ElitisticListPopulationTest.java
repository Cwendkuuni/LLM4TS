import static org.junit.Assert.*;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> oldChromosomes = new ArrayList<Chromosome>();
        oldChromosomes.add(new Chromosome(1));
        oldChromosomes.add(new Chromosome(2));
        oldChromosomes.add(new Chromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 3, 0.9);

        Population nextGeneration = population.nextGeneration();

        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        population.setElitismRate(0.8);

        assertEquals(0.8, population.getElitismRate(), 0.001);
    }
}