import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
    }

    @After
    public void tearDown() {
        elitisticListPopulation = null;
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            chromosomes.add(new Chromosome());
        }
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();
        Assert.assertEquals(9, nextGeneration.getChromosomes().size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        elitisticListPopulation.setElitismRate(1.5);
    }

    @Test
    public void testSetElitismRate() {
        elitisticListPopulation.setElitismRate(0.5);
        Assert.assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.001);
    }

}