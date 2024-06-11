import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {
    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setUp() {
        elitisticListPopulation = new ElitisticListPopulation(100, 0.5);
    }

    @Test
    public void testConstructor() {
        assertEquals(100, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.0001);
    }

    @Test
    public void testSetElitismRate() {
        elitisticListPopulation.setElitismRate(0.3);
        assertEquals(0.3, elitisticListPopulation.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        elitisticListPopulation.setElitismRate(-0.1);
        elitisticListPopulation.setElitismRate(1.1);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(100, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.0001);
        assertTrue(nextGeneration.getChromosomes().isEmpty());
    }

    @Test
    public void testNextGenerationWithChromosomes() {
        elitisticListPopulation.addChromosome(new Chromosome());
        elitisticListPopulation.addChromosome(new Chromosome());
        elitisticListPopulation.addChromosome(new Chromosome());

        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();
        assertEquals(100, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.0001);
        assertEquals(0, nextGeneration.getChromosomes().size());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        elitisticListPopulation.addChromosome(chromosome);
        assertTrue(elitisticListPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test
    public void testRemoveChromosome() {
        Chromosome chromosome = new Chromosome();
        elitisticListPopulation.addChromosome(chromosome);
        elitisticListPopulation.removeChromosome(chromosome);
        assertFalse(elitisticListPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test
    public void testGetChromosome() {
        Chromosome chromosome = new Chromosome();
        elitisticListPopulation.addChromosome(chromosome);
        assertEquals(chromosome, elitisticListPopulation.getChromosome(0));
    }
    
    @Test
    public void testGetChromosomeInvalidIndex() {
        assertNull(elitisticListPopulation.getChromosome(10));
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(100, elitisticListPopulation.getPopulationLimit());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, elitisticListPopulation.getPopulationSize());
    }
}