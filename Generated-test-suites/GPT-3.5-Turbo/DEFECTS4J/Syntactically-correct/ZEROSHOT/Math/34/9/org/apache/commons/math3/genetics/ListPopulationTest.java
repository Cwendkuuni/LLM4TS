package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

   private ListPopulation listPopulation;
   private List<Chromosome> chromosomes;
   private Chromosome chromosome1;
   private Chromosome chromosome2;
   private Chromosome chromosome3;

   @Before
   public void setUp() {
      chromosomes = new ArrayList<Chromosome>();
      chromosome1 = new Chromosome();
      chromosome2 = new Chromosome();
      chromosome3 = new Chromosome();
      chromosomes.add(chromosome1);
      chromosomes.add(chromosome2);
      chromosomes.add(chromosome3);
      
      listPopulation = new ListPopulation(chromosomes, 5);  
   }

   @Test
   public void testSetChromosomes() {
      List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
      Chromosome newChromosome1 = new Chromosome();
      Chromosome newChromosome2 = new Chromosome();
      newChromosomes.add(newChromosome1);
      newChromosomes.add(newChromosome2);
      
      listPopulation.setChromosomes(newChromosomes);
      
      assertEquals(newChromosomes, listPopulation.getChromosomes());
      assertEquals(newChromosomes.size(), listPopulation.getPopulationSize());
   }
   
   @Test(expected = NullArgumentException.class)
   public void testSetChromosomesNullArgument() {
      List<Chromosome> nullChromosomes = null;
      
      listPopulation.setChromosomes(nullChromosomes);
   }
   
   @Test(expected = NumberIsTooLargeException.class)
   public void testSetChromosomesNumberIsTooLargeException() {
      List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
      Chromosome newChromosome1 = new Chromosome();
      Chromosome newChromosome2 = new Chromosome();
      Chromosome newChromosome3 = new Chromosome();
      newChromosomes.addAll(Arrays.asList(newChromosome1, newChromosome2, newChromosome3));
      
      listPopulation.setChromosomes(newChromosomes);
   }

   @Test
   public void testAddChromosomes() {
      List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
      Chromosome newChromosome1 = new Chromosome();
      Chromosome newChromosome2 = new Chromosome();
      newChromosomes.addAll(Arrays.asList(newChromosome1, newChromosome2));
      
      listPopulation.addChromosomes(newChromosomes);
      
      assertEquals(chromosomes.size() + newChromosomes.size(), listPopulation.getPopulationSize());
   }
   
   @Test(expected = NumberIsTooLargeException.class)
   public void testAddChromosomesNumberIsTooLargeException() {
      List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
      Chromosome newChromosome1 = new Chromosome();
      Chromosome newChromosome2 = new Chromosome();
      Chromosome newChromosome3 = new Chromosome();
      newChromosomes.addAll(Arrays.asList(newChromosome1, newChromosome2, newChromosome3));
      
      listPopulation.addChromosomes(newChromosomes);
   }
   
   @Test
   public void testGetChromosomes() {
      List<Chromosome> result = listPopulation.getChromosomes();
      
      assertEquals(chromosomes, result);
   }

   @Test
   public void testGetChromosomeList() {
      List<Chromosome> result = listPopulation.getChromosomeList();
      
      assertEquals(chromosomes, result);
   }

   @Test
   public void testAddChromosome() {
      Chromosome newChromosome = new Chromosome();
      
      listPopulation.addChromosome(newChromosome);
      
      assertEquals(chromosomes.size() + 1, listPopulation.getPopulationSize());
   }
   
   @Test(expected = NumberIsTooLargeException.class)
   public void testAddChromosomeNumberIsTooLargeException() {
      Chromosome newChromosome = new Chromosome();
      listPopulation.setPopulationLimit(chromosomes.size());
      
      listPopulation.addChromosome(newChromosome);
   }

   @Test
   public void testGetFittestChromosome() {
      Chromosome result = listPopulation.getFittestChromosome();
      
      assertEquals(chromosome3, result);
   }

   @Test
   public void testGetPopulationLimit() {
      int result = listPopulation.getPopulationLimit();
      
      assertEquals(5, result);
   }

   @Test
   public void testSetPopulationLimit() {
      int newPopulationLimit = 10;
      
      listPopulation.setPopulationLimit(newPopulationLimit);
      
      assertEquals(newPopulationLimit, listPopulation.getPopulationLimit());
   }
   
   @Test(expected = NotPositiveException.class)
   public void testSetPopulationLimitNotPositiveException() {
      int newPopulationLimit = 0;
      
      listPopulation.setPopulationLimit(newPopulationLimit);
   }
   
   @Test(expected = NumberIsTooSmallException.class)
   public void testSetPopulationLimitNumberIsTooSmallException() {
      listPopulation.setPopulationLimit(chromosomes.size() - 1);
   }
   
   @Test
   public void testGetPopulationSize() {
      int result = listPopulation.getPopulationSize();
      
      assertEquals(chromosomes.size(), result);
   }

   @Test
   public void testToString() {
      String result = listPopulation.toString();
      
      assertEquals(chromosomes.toString(), result);
   }

   @Test
   public void testIterator() {
      assertNotNull(listPopulation.iterator());
   }
   
   private class Chromosome implements Comparable<Chromosome> {
      @Override
      public int compareTo(Chromosome o) {
         return 0;
      }
   }
}