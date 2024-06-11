import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    // Test basic functionality of ElitisticListPopulation constructor with list of chromosomes
    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(chromosomes, elitisticListPopulation.getChromosomes());
    }

    // Test basic functionality of ElitisticListPopulation constructor without list of chromosomes
    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals(0, elitisticListPopulation.getChromosomes().size());
    }

    // Test nextGeneration method with typical use case
    @Test
    public void testNextGenerationTypical() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        Assert.assertEquals(1, nextGeneration.getChromosomes().size());
    }

    // Test setElitismRate method with valid and invalid input
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.9);
        elitisticListPopulation.setElitismRate(0.8);
        Assert.assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.0);
        
        // Test for OutOfRangeException
        try {
            elitisticListPopulation.setElitismRate(-0.1);
            Assert.fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            Assert.assertEquals(LocalizedFormats.ELITISM_RATE.toString(), e.getSpecificPattern());
        }
    }

}