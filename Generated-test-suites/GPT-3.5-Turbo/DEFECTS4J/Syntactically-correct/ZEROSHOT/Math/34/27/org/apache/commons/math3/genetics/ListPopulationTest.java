import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {
    
    private ListPopulation listPopulation;
    
    @Before
    public void setup() {
        listPopulation = new ListPopulation(10);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        
        assertEquals(2, listPopulation.getChromosomes().size());
    }
    
    @Test
    public void testGetChromosomes() {
        assertNotNull(listPopulation.getChromosomes());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        
        chromosome1.setFitness(10);
        chromosome2.setFitness(5);
        
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, listPopulation.getPopulationLimit());
    }
    
    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(20);
        
        assertEquals(20, listPopulation.getPopulationLimit());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(listPopulation.iterator());
    }
    
}