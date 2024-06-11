import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(5, initialChromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(initialChromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesExceedingLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        initialChromosomes.add(chromosome3);

        ListPopulation population = new ListPopulation(3, initialChromosomes);
    }

    @Test
    public void testListPopulationWithAddingChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);

        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosomes(Collections.singletonList(chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(Arrays.asList(chromosome1, chromosome2)));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddingChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);

        population.addChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
    }

    @Test
    public void testListPopulationWithSettingChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        population.setChromosomes(initialChromosomes);
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(initialChromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithSettingChromosomesExceedingLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);
        initialChromosomes.add(chromosome3);

        population.setChromosomes(initialChromosomes);
    }

    @Test
    public void testListPopulationWithGettingFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome bestChromosome = new DummyChromosome(10);
        population.addChromosome(bestChromosome);
        Chromosome otherChromosome = new DummyChromosome(5);
        population.addChromosome(otherChromosome);

        Chromosome result = population.getFittestChromosome();
        assertEquals(bestChromosome, result);
    }

    @Test
    public void testListPopulationWithSettingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationWithSettingPopulationLimitSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        StringBuilder expected = new StringBuilder();
        expected.append("[");
        expected.append(chromosome1.toString());
        expected.append(", ");
        expected.append(chromosome2.toString());
        expected.append("]");

        assertEquals(expected.toString(), population.toString());
    }

    @Test
    public void testListPopulationWithIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome result1 = iterator.next();
        Chromosome result2 = iterator.next();

        assertEquals(chromosome1, result1);
        assertEquals(chromosome2, result2);
        assertFalse(iterator.hasNext());
    }
}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Chromosome o) {
        if (o == null) {
            return 1;
        }
        return Integer.compare(this.id, o.getId());
    }

    @Override
    public int getId() {
        return id;
    }
}