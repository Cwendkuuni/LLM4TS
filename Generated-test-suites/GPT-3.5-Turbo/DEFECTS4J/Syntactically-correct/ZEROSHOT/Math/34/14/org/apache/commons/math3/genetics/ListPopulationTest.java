import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {
    
    // Test SetChromosomes() method
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setChromosomes(chromosomes);
        
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    // Test AddChromosomes() method
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosomes(chromosomes);
        
        assertEquals(chromosomes.size(), listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().containsAll(chromosomes));
    }
    
    // Test getChromosomes() method
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosomes(chromosomes);
        
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    // Test getChromosomeList() method
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosomes(chromosomes);
        
        assertEquals(chromosomes, listPopulation.getChromosomeList());
    }
    
    // Test addChromosome() method
    @Test
    public void testAddChromosome() {
        Chromosome ch1 = new Chromosome();
        
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosome(ch1);
        
        assertTrue(listPopulation.getChromosomes().contains(ch1));
    }
    
    // Test getFittestChromosome() method
    @Test
    public void testGetFittestChromosome() {
        Chromosome ch1 = new Chromosome();
        ch1.setFitness(10);
        Chromosome ch2 = new Chromosome();
        ch2.setFitness(5);
        Chromosome ch3 = new Chromosome();
        ch3.setFitness(15);
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        chromosomes.add(ch3);
        
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        
        assertEquals(ch3, listPopulation.getFittestChromosome());
    }
    
    // Test getPopulationLimit() method
    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        
        assertEquals(10, listPopulation.getPopulationLimit());
    }
    
    // Test setPopulationLimit() method
    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(20);
        
        assertEquals(20, listPopulation.getPopulationLimit());
    }
    
    // Test getPopulationSize() method
    @Test
    public void testGetPopulationSize() {
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        
        assertEquals(2, listPopulation.getPopulationSize());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        
        assertEquals(chromosomes.toString(), listPopulation.toString());
    }
    
    // Test iterator() method
    @Test
    public void testIterator() {
        Chromosome ch1 = new Chromosome();
        Chromosome ch2 = new Chromosome();
        
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(ch1);
        chromosomes.add(ch2);
        
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        
        Iterator<Chromosome> iterator = listPopulation.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ch1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ch2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}