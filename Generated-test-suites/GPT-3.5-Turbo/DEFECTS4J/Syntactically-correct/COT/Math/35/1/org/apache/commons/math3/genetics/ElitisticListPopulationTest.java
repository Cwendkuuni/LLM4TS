import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

    // Test case for ElitisticListPopulation constructor with initialized chromosomes
    @Test
    public void testElitisticListPopulationConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        assertEquals(1, population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0);
    }

    // Test case for ElitisticListPopulation constructor with uninitialized chromosomes
    @Test
    public void testElitisticListPopulationEmptyConstructor() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        assertEquals(0, population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0);
    }

    // Test case for nextGeneration method
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 2, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
        assertEquals(2, nextGeneration.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0);
    }

    // Test case for setElitismRate method
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        population.setElitismRate(0.3);
        assertEquals(0.3, population.getElitismRate(), 0);
    }

}