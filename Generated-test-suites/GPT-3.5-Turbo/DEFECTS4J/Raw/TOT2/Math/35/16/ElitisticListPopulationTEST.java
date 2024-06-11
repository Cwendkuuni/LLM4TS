import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        assertEquals(chromosomes.size(), population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructor2() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        Population nextGen = population.nextGeneration();
        assertEquals(1, nextGen.getChromosomes().size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        population.setElitismRate(1.5);
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

}