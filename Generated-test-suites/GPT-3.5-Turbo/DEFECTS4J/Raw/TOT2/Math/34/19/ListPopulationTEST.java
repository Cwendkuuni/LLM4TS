import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        chromosome.setFitness(10);
        listPopulation.addChromosome(chromosome);

        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        assertSame(chromosome, fittestChromosome);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(chromosomes);
        assertEquals(5, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesPopulationLimitExceeded() {
        ListPopulation listPopulation = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(chromosomes);
        assertEquals(5, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesPopulationLimitExceeded() {
        ListPopulation listPopulation = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(chromosomes);
    }
}