package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    
    private ListPopulation population;
    
    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertNotNull(population.getChromosomes());
    }
    
    @Test
    public void testSetChromosomes() {
        assertTrue(population.getChromosomes().isEmpty());
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
        
        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
        assertTrue(population.getChromosomes().contains(chromosome3));
    }
    
    @Test
    public void testAddChromosomes() {
        assertTrue(population.getChromosomes().isEmpty());
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }
    
    @Test
    public void testGetFittestChromosome() {
        assertNull(population.getFittestChromosome());
        
        Chromosome chromosome1 = new Chromosome(1);
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome(2);
        chromosome2.setFitness(20);
        Chromosome chromosome3 = new Chromosome(3);
        chromosome3.setFitness(15);
        
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        assertEquals(chromosome2, population.getFittestChromosome());
    }
    
    @Test
    public void testSetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
        
        population.setPopulationLimit(10);
        
        assertEquals(10, population.getPopulationLimit());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testIterator() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        
        Iterator<Chromosome> iterator = population.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(chromosome, iterator.next());
        assertFalse(iterator.hasNext());
    }
}