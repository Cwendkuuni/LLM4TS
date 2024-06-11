import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {
    
    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithPopulationLimitNegative() {
        new ListPopulation(-10);
    }
    
    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertNotNull(population);
        assertEquals(2, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithChromosomesAndPopulationLimitNullChromosomes() {
        new ListPopulation(null, 10);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithChromosomesAndPopulationLimitNegative() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        new ListPopulation(chromosomes, -10);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesAndPopulationLimitExceeded() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        new ListPopulation(chromosomes, 2);
    }
    
    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceeded() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        Chromosome chromosome3 = new Chromosome();
        Chromosome chromosome4 = new Chromosome();
        additionalChromosomes.add(chromosome3);
        additionalChromosomes.add(chromosome4);
        
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        population.addChromosomes(additionalChromosomes);
        
        assertEquals(4, population.getPopulationSize());
        assertEquals(chromosomes.size() + additionalChromosomes.size(), population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes().subList(0, chromosomes.size()));
        assertEquals(additionalChromosomes, population.getChromosomes().subList(chromosomes.size(), population.getPopulationSize()));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceeded() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        additionalChromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(chromosomes);
        population.addChromosomes(additionalChromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test
    public void testGetChromosomeList() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomeList());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.addChromosome(chromosome3);
        assertEquals(3, population.getPopulationSize());
        assertEquals(chromosome3, population.getChromosomes().get(2));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceeded() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(2);
        population.setChromosomes(chromosomes);
        population.addChromosome(chromosome3);
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(15);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = population.getFittestChromosome();
        
        assertEquals(chromosome2, fittestChromosome);
        assertEquals(20, fittestChromosome.getFitness());
    }
    
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }
    
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-10);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmaller() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(5);
    }
    
    @Test
    public void testGetPopulationSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes.toString(), population.toString());
    }
    
    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        assertEquals(chromosomes.size(), count);
    }
    
}