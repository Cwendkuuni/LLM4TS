import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        int populationLimit = 100;

        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getChromosomes().size());
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        int populationLimit = 100;

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(2, listPopulation.getPopulationSize());
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        int populationLimit = 100;

        ListPopulation listPopulation = new ListPopulation(null, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        int populationLimit = -100;

        ListPopulation listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithZeroPopulationLimit() {
        int populationLimit = 0;

        ListPopulation listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithChromosomesExceedingPopulationLimit() {
        int populationLimit = 2;

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }
    
    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(100);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getPopulationSize());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(100);

        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome());

        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(100);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.addChromosomes(chromosomes);
        assertEquals(2, listPopulation.getPopulationSize());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(2, listPopulation.getChromosomes().size());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(100);

        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(5);
        Chromosome chromosome3 = new Chromosome(20);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        assertEquals(chromosome3, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 100;

        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        ListPopulation listPopulation = new ListPopulation(100);

        listPopulation.setPopulationLimit(-100);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroLimit() {
        ListPopulation listPopulation = new ListPopulation(100);

        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithTooSmallLimit() {
        ListPopulation listPopulation = new ListPopulation(100);

        listPopulation.setPopulationLimit(50);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(100);

        assertEquals(0, listPopulation.getPopulationSize());

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(100);

        assertEquals("[]", listPopulation.toString());

        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(5);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        assertEquals("[Chromosome(10), Chromosome(5)]", listPopulation.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(100);

        assertFalse(listPopulation.iterator().hasNext());

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}