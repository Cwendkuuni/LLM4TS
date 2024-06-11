import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertFalse(population.isFull());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationSetterWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetterWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new ChromosomeImpl(); // Assuming ChromosomeImpl is a valid Chromosome implementation
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWhenPopulationIsFull() {
        ListPopulation population = new ListPopulation(1);
        Chromosome chromosome = new ChromosomeImpl();
        population.addChromosome(chromosome);
        population.addChromosome(new ChromosomeImpl());
        try {
            population.addChromosome(new ChromosomeImpl());
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome best = new ChromosomeImpl();
        Chromosome worst = new ChromosomeImpl();
        Chromosome intermediate = new ChromosomeImpl();

        population.addChromosome(best);
        population.addChromosome(worst);
        population.addChromosome(intermediate);

        assertEquals(best, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new ChromosomeImpl();
        Chromosome chromosome2 = new ChromosomeImpl();
        Chromosome chromosome3 = new ChromosomeImpl();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        String result = population.toString();
        String expected = "[Lorg.apache.commons.math3.genetics.Chromosome;@" + result.substring(7) + "]";
        assertEquals(expected, result);
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new ChromosomeImpl();
        Chromosome chromosome2 = new ChromosomeImpl();
        Chromosome chromosome3 = new ChromosomeImpl();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        Chromosome next = iterator.next();
        assertEquals(chromosome1, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(chromosome2, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(chromosome3, next);
        assertFalse(iterator.hasNext());
    }
}