import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGen = elitisticListPopulation.nextGeneration();
        assertNotNull(nextGen);
    }

    @Test
    public void testSetElitismRate() {
        elitisticListPopulation.setElitismRate(0.8);
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        elitisticListPopulation.setElitismRate(-0.5);
    }

}