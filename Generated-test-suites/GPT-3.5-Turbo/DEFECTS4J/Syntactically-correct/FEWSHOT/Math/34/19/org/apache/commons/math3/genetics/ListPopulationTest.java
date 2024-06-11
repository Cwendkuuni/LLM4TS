
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListPopulationTest {
    
    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals(3, population.getPopulationSize());
        assertEquals(3, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NotPositiveException() {
        ListPopulation population = new ListPopulation(-1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 2);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_NullArgumentException() {
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_NumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(3);
        population.addChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_NumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(2);
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        
        ListPopulation population = new ListPopulation(3);
        population.addChromosome(chromosome);
        
        assertEquals(1, population.getPopulationSize());
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_NumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 3);
        population.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(50);
        
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(100);
        
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(75);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(chromosomes, 3);
        
        assertEquals(chromosome2, population.getFittestChromosome());
    }
    
    @Test
    public void testGetFittestChromosome_EqualFitness() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(50);
        
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(50);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 2);
        
        assertEquals(chromosome1, population.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }
    
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_NotPositiveException() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_NumberIsTooSmallException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 2);
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testToString() {
        Chromosome chromosome = new Chromosome();
        chromosome.setFitness(50);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        
        ListPopulation population = new ListPopulation(chromosomes, 5);
        
        assertEquals(chromosomes.toString(), population.toString());
    }
    
    @Test
    public void testIterator() {
        Chromosome chromosome = new Chromosome();
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        
        ListPopulation population = new ListPopulation(chromosomes, 5);
        
        int count = 0;
        for (Chromosome c : population) {
            assertEquals(chromosome, c);
            count++;
        }
        
        assertEquals(1, count);
    }
    
    @Test
    public void testIterator_Empty() {
        ListPopulation population = new ListPopulation(5);
        
        int count = 0;
        for (Chromosome c : population) {
            count++;
        }
        
        assertEquals(0, count);
    }
    
    @Test
    public void testIterator_Remove() {
        Chromosome chromosome = new Chromosome();
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        
        ListPopulation population = new ListPopulation(chromosomes, 5);
        
        Iterator<Chromosome> iter = population.iterator();
        iter.remove();
        
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove_UnsupportedOperationException() {
        ListPopulation population = new ListPopulation(5);
        
        Iterator<Chromosome> iter = population.iterator();
        iter.remove();
    }
}
