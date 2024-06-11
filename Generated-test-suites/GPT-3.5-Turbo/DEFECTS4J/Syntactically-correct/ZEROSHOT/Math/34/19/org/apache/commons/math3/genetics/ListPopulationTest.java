import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes.size(), population.getPopulationSize());
    }
    
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertNotNull(chromosomes);
    }
    
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomeList = population.getChromosomeList();
        assertNotNull(chromosomeList);
    }
    
    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }
    
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosome1.setFitness(0.5);
        chromosome2.setFitness(0.2);
        chromosome3.setFitness(0.8);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        assertEquals(chromosome3, population.getFittestChromosome());
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
    
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        Iterator<Chromosome> iterator = population.iterator();
        assertNotNull(iterator);
    }
}