import org.junit.Test;
import org.apache.commons.math3.genetics.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        // Test case 1: population limit is positive number
        ListPopulation population1 = new ListPopulation(10);
        assertEquals(10, population1.getPopulationLimit());

        // Test case 2: population limit is zero
        try {
            ListPopulation population2 = new ListPopulation(0);
            fail("Expected NotPositiveException to be thrown");
        } catch (NotPositiveException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddChromosome() {
        // Test case 1: population not full
        ListPopulation population1 = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        population1.addChromosome(chromosome1);
        assertEquals(1, population1.getPopulationSize());

        // Test case 2: population already full
        ListPopulation population2 = new ListPopulation(1);
        Chromosome chromosome2 = new Chromosome();
        population2.addChromosome(chromosome2);
        try {
            population2.addChromosome(chromosome2);
            fail("Expected NumberIsTooLargeException to be thrown");
        } catch (NumberIsTooLargeException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetFittestChromosome() {
        // Test case 1: population not empty
        ListPopulation population1 = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        population1.addChromosome(chromosome1);
        population1.addChromosome(chromosome2);
        population1.addChromosome(chromosome3);
        assertEquals(chromosome1, population1.getFittestChromosome());

        // Test case 2: population empty
        ListPopulation population2 = new ListPopulation(1);
        try {
            population2.getFittestChromosome();
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetPopulationSize() {
        // Test case 1: population not empty
        ListPopulation population1 = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        population1.addChromosome(chromosome1);
        population1.addChromosome(chromosome2);
        population1.addChromosome(chromosome3);
        assertEquals(3, population1.getPopulationSize());

        // Test case 2: population empty
        ListPopulation population2 = new ListPopulation(1);
        assertEquals(0, population2.getPopulationSize());
    }

    @Test
    public void testToString() {
        // Test case 1: population not empty
        ListPopulation population1 = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        population1.addChromosome(chromosome1);
        population1.addChromosome(chromosome2);
        population1.addChromosome(chromosome3);
        assertEquals("[chromosome1, chromosome2, chromosome3]", population1.toString());

        // Test case 2: population empty
        ListPopulation population2 = new ListPopulation(1);
        assertEquals("[]", population2.toString());
    }
}