import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructorWithPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        int populationLimit = 10;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected=NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(null, populationLimit);
    }

    @Test(expected=NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        int populationLimit = -1;
        ListPopulation population = new ListPopulation(populationLimit);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testConstructorWithExceededChromosomes() {
        int populationLimit = 2;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected=NullArgumentException.class)
    public void testSetNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testSetExceededChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
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
        
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testAddExceededChromosomes() {
        ListPopulation population = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomes();
        
        assertNotNull(chromosomes);
        assertEquals(0, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        
        assertNotNull(chromosomes);
        assertEquals(0, chromosomes.size());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected=NumberIsTooLargeException.class)
    public void testAddExceededChromosome() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome fittestChromosome = population.getFittestChromosome();
        
        assertNull(fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        
        assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected=NotPositiveException.class)
    public void testSetNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-5);
    }

    @Test(expected=NumberIsTooSmallException.class)
    public void testSetSmallerPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(10);
        String populationString = population.toString();
        
        assertNotNull(populationString);
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        Iterator<Chromosome> iterator = population.iterator();
        
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }
}