
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private static final int POPULATION_LIMIT = 10;
    
    private ListPopulation population;
    
    @Before
    public void setUp() {
        population = new ListPopulation(POPULATION_LIMIT);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(POPULATION_LIMIT, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidLimit() {
        new ListPopulation(0);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(chromosomes.size(), population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithInvalidSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        
        population.setChromosomes(chromosomes);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(chromosomes.size(), population.getPopulationSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNull() {
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithInvalidSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        
        population.setChromosomes(chromosomes);
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        
        population.addChromosome(chromosome);
        
        assertEquals(Arrays.asList(chromosome), population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithInvalidSize() {
        Chromosome chromosome = new Chromosome();
        
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            population.addChromosome(new Chromosome());
        }
        
        population.addChromosome(chromosome);
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(1.0);
        Chromosome chromosome3 = new Chromosome();
        
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        assertEquals(chromosome2, population.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(POPULATION_LIMIT, population.getPopulationLimit());
    }
    
    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);
        
        assertEquals(20, population.getPopulationLimit());
    }
    
    @Test
    public void testSetPopulationLimitWithNewSizeSmallerThanCurrentSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
        
        population.setPopulationLimit(1);
        
        assertEquals(1, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidLimit() {
        population.setPopulationLimit(-1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithNewSizeSmallerThanCurrentSize() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
        
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test
    public void testToString() {
        assertEquals("[]", population.toString());
        
        population.addChromosome(new Chromosome());
        
        assertEquals("[Chromosome]", population.toString());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(population.iterator());
    }
}
