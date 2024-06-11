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
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(Collections.singletonList(chromosome1));
        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1.getFitness(), fittestChromosome.getFitness());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithInitialChromosomesExceedingPopulationLimit() {
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        ListPopulation population = new ListPopulation(new ArrayList<>(), 2);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
    }

    @Test
    public void testListPopulationWithAddChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals(3, population.getPopulationSize());
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1.getFitness(), fittestChromosome.getFitness());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
    }

    @Test
    public void testListPopulationWithSetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3));
        assertEquals(3, population.getPopulationSize());
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1.getFitness(), fittestChromosome.getFitness());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithSetChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        population.setChromosomes(Arrays.asList(chromosome1, chromosome2, chromosome3, new MockChromosome(4)));
    }

    @Test
    public void testListPopulationWithChangePopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        population.setPopulationLimit(10);
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimitChange() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationWithChangePopulationLimitBelowCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        assertEquals("[Chromosome[fitness=0.0], Chromosome[fitness=0.0]]", population.toString());
    }
}

class MockChromosome implements Chromosome {
    private final int id;
    private final double fitness;

    public MockChromosome(int id) {
        this.id = id;
        this.fitness = Math.random();
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MockChromosome other = (MockChromosome) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Chromosome[fitness=" + fitness + "]";
    }
}