import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertTrue(actualChromosomes.containsAll(initialChromosomes));
        assertEquals(actualChromosomes.size(), population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, -1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullInitialChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesGreaterThanPopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        initialChromosomes.add(chromosome3);

        ListPopulation population = new ListPopulation(initialChromosomes, 2);
    }

    @Test
    public void testListPopulationWithAddChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosome(chromosome2);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertTrue(actualChromosomes.containsAll(Arrays.asList(chromosome1, chromosome2)));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddChromosomesGreaterThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosome(chromosome2);
        Chromosome chromosome3 = new MockChromosome(3);
        population.addChromosome(chromosome3);
    }

    @Test
    public void testListPopulationWithSetChromosomesDeprecated() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(initialChromosomes);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertTrue(actualChromosomes.containsAll(initialChromosomes));
        assertEquals(actualChromosomes.size(), population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomesToAdd() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithChromosomesToAddGreaterThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        Collection<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosome1);
        chromosomesToAdd.add(chromosome2);
        chromosomesToAdd.add(chromosome3);
        population.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testListPopulationWithAddChromosomesToEmptyPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosomes(Arrays.asList(chromosome2));

        assertEquals(5, population.getPopulationLimit());
        assertEquals(6, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertTrue(actualChromosomes.containsAll(Arrays.asList(chromosome1, chromosome2)));
    }

    @Test
    public void testListPopulationWithAddChromosomesToPopulationWithChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        Chromosome chromosome3 = new MockChromosome(3);
        population.addChromosome(chromosome3);
        Chromosome chromosome4 = new MockChromosome(4);
        population.addChromosomes(Arrays.asList(chromosome4));

        assertEquals(5, population.getPopulationLimit());
        assertEquals(8, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertTrue(actualChromosomes.containsAll(Arrays.asList(chromosome1, chromosome2, chromosome3, chromosome4)));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new MockChromosome(1));
        population.addChromosome(new MockChromosome(2));
        population.addChromosome(new MockChromosome(3));
        population.addChromosome(new MockChromosome(4));
        population.addChromosome(new MockChromosome(5));

        Chromosome fittestChromosome = population.getFittestChromosome();

        assertTrue(population.getChromosomes().contains(fittestChromosome));
        assertTrue(fittestChromosome.compareTo(population.getChromosomes().get(0)) >= 0);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativeNumber() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerNumber() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new MockChromosome(1));
        population.addChromosome(new MockChromosome(2));
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new MockChromosome(1));
        population.addChromosome(new MockChromosome(2));
        population.addChromosome(new MockChromosome(3));
        population.addChromosome(new MockChromosome(4));
        population.addChromosome(new MockChromosome(5));

        String result = population.toString();
        String expected = "[Chromosome: 5, Chromosome: 4, Chromosome: 3, Chromosome: 2, Chromosome: 1]";
        assertEquals(expected, result);
    }
}

class MockChromosome implements Chromosome {
    private int value;

    public MockChromosome(int value) {
        this.value = value;
    }

    @Override
    public int getFitness() {
        return value;
    }

    @Override
    public int compareTo(Chromosome other) {
        MockChromosome otherChromosome = (MockChromosome) other;
        return Integer.compare(otherChromosome.value, this.value);
    }
}