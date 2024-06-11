import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        ElitisticListPopulation eListPopulation = new ElitisticListPopulation(10, 0.9);
        assertNotNull(eListPopulation);
        assertEquals(10, eListPopulation.getPopulationLimit());
        assertEquals(0.9, eListPopulation.getElitismRate(), 0.0001);
        assertEquals(0, eListPopulation.getChromosomes().size());
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation eListPopulation = new ElitisticListPopulation(10, 0.9);
        ElitisticListPopulation nextGeneration = eListPopulation.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(eListPopulation.getPopulationLimit(), nextGeneration.getPopulationLimit());
        assertEquals(eListPopulation.getElitismRate(), nextGeneration.getElitismRate(), 0.0001);
        assertEquals(0, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation eListPopulation = new ElitisticListPopulation(10, 0.9);
        eListPopulation.setElitismRate(0.8);
        assertEquals(0.8, eListPopulation.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidValue() {
        ElitisticListPopulation eListPopulation = new ElitisticListPopulation(10, 0.9);
        eListPopulation.setElitismRate(-0.5);
    }

}