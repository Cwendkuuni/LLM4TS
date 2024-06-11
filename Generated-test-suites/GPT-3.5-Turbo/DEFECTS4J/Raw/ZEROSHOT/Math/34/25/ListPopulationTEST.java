import static org.junit.Assert.*;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        try {
            new ListPopulation(emptyList, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        try {
            new ListPopulation(null, 1);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
        try {
            new ListPopulation(emptyList, 0);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        try {
            new ListPopulation(emptyList, 2);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getChromosomes().size());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        Collection<Chromosome> chromosomeColl = new ArrayList<Chromosome>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        assertEquals(2, population.getChromosomes().size());
        assertTrue(population.getChromosomes().containsAll(chromosomeColl));
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getChromosomes().size());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(2.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(3.0);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(1.0);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        assertEquals(3, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        population.setPopulationLimit(2);
        assertEquals(2, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        assertEquals(0, population.getPopulationSize());
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(2.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(3.0);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(1.0);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        String expectedString = "[" + chromosome1.toString() + ", " + chromosome2.toString() + ", " + chromosome3.toString() + "]";
        assertEquals(expectedString, population.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> emptyList = Collections.<Chromosome> emptyList();
        ListPopulation population = new ListPopulation(emptyList, 3);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(2.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(3.0);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(1.0);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome3, iterator.next());
        assertFalse(iterator.hasNext());
    }

}