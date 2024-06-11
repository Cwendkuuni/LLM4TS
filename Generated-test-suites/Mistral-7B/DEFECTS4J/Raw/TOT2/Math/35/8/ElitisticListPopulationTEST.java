
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationDefaultConstructor() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        assertEquals(0, population.size());
    }

    @Test
    public void testElitisticListPopulationWithChromosomesAndPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(null, 10);
        assertNull(population);

        ElitisticListPopulation population1 = new ElitisticListPopulation(new Chromosome[0], 10);
        assertEquals(10, population1.size());

        ElitisticListPopulation population2 = new ElitisticListPopulation(Arrays.asList(new Chromosome[0]), 0);
        assertEquals(0, population2.size());

        ElitisticListPopulation population3 = new ElitisticListPopulation(Arrays.asList(new Chromosome[0]), -1);
        assertEquals(0, population3.size());
    }

    @Test
    public void testElitisticListPopulationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 1.1);
        assertEquals(10, population.size());

        ElitisticListPopulation population1 = new ElitisticListPopulation(new ArrayList<>(), 10, 0);
        assertEquals(10, population1.size());

        ElitisticListPopulation population2 = new ElitisticListPopulation(new ArrayList<>(), 10, 2);
        assertEquals(10, population2.size());

        ElitisticListPopulation population3 = new ElitisticListPopulation(new ArrayList<>(), 10, 1.2);
        assertEquals(10, population3.size());

        ElitisticListPopulation population4 = new ElitisticListPopulation(new ArrayList<>(), 10, 1.3);
        assertEquals(10, population4.size());

        ElitisticListPopulation population5 = new ElitisticListPopulation(new ArrayList<>(), 10, 2.1);
        assertEquals(10, population5.size());

        ElitisticListPopulation population6 = new ElitisticListPopulation(new ArrayList<>(), 10, -0.1);
        assertEquals(10, population6.size());

        ElitisticListPopulation population7 = new ElitisticListPopulation(new ArrayList<>(), 10, 1.5);
        assertEquals(10, population7.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateLessThanZero() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        population.setElitismRate(2.1);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());

        ElitisticListPopulation population1 = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        Population nextGeneration1 = population1.nextGeneration();
        assertEquals(5, nextGeneration1.size());

        ElitisticListPopulation population2 = new ElitisticListPopulation(new ArrayList<>(), 15, 0.9);
        Population nextGeneration2 = population2.nextGeneration();
        assertEquals(10, nextGeneration2.size());
    }

}