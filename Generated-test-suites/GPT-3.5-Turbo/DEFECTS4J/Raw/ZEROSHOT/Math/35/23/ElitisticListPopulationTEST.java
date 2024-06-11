import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        population = new ElitisticListPopulation(10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(2, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

}