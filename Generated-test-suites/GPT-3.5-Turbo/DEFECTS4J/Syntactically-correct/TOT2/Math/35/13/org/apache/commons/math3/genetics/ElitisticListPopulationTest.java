import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(2, population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertNotNull(population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        ListPopulation nextGen = population.nextGeneration();
        assertEquals(1, nextGen.getChromosomes().size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.5);
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

}