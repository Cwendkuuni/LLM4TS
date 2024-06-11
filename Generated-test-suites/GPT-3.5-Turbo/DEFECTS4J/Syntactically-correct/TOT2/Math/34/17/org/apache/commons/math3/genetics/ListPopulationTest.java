import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(new ArrayList<Integer>());
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(chromosome, listPopulation.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimitExceeded() {
        ListPopulation listPopulation = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome(new ArrayList<Integer>());
        Chromosome chromosome2 = new Chromosome(new ArrayList<Integer>());
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(new ArrayList<Integer>());
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome(new ArrayList<Integer>());
        chromosome2.setFitness(20);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(new ArrayList<Integer>());
        listPopulation.addChromosome(chromosome);
        listPopulation.setPopulationLimit(1);
    }
}