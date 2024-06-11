import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ListPopulationTest {
    
    @Test
    public void testConstructor() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(100);
        population.addChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertNotNull(populationChromosomes);
        assertEquals(2, populationChromosomes.size());
    }
    
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        population.setPopulationLimit(200);
        
        assertEquals(200, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation(100);
        population.setPopulationLimit(-100);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        chromosome1.setFitness(0);
        
        Chromosome chromosome2 = new Chromosome(10);
        chromosome2.setFitness(5);
        
        Chromosome chromosome3 = new Chromosome(10);
        chromosome3.setFitness(10);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        Chromosome fittestChromosome = population.getFittestChromosome();
        
        assertNotNull(fittestChromosome);
        assertEquals(chromosome3, fittestChromosome);
    }
    
    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        
        assertNotNull(population.toString());
    }

}