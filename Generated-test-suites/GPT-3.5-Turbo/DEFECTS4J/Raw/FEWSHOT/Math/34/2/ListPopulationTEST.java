
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testConstructorAndGetters() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        new ListPopulation(0);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(chromosomes);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetChromosomesWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(new ArrayList<Chromosome>());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(null);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(3);
        population.addChromosomes(chromosomes);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosomes(new ArrayList<Chromosome>());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        
        ListPopulation population = new ListPopulation(2);
        population.addChromosome(chromosome);
        
        List<Chromosome> expected = new ArrayList<Chromosome>();
        expected.add(chromosome);
        assertEquals(expected, population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(15);
        
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        
        assertEquals(chromosome3, population.getFittestChromosome());
    }
    
    @Test
    public void testGetFittestChromosomeWithEmptyPopulation() {
        ListPopulation population = new ListPopulation(10);
        assertNull(population.getFittestChromosome());
    }
    
    @Test
    public void testSetAndGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
        
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(5);
    }
    
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        
        assertEquals("[Chromosome, Chromosome]", population.toString());
    }
    
    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        
        assertEquals(2, count);
    }
}
