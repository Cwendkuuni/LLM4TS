import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class ListPopulationTest {
    
    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.setChromosomes(chromosomes);
        
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        ListPopulation population = new ListPopulation(10);
        
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
        
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.addAll(chromosomes);
        
        Assert.assertEquals(expectedChromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(0.8);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.9);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        
        Chromosome fittestChromosome = population.getFittestChromosome();
        
        Assert.assertEquals(chromosome2, fittestChromosome);
    }
    
    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        ListPopulation population = new ListPopulation(10);
        
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        
        Assert.assertEquals(2, population.getPopulationSize());
    }
    
}