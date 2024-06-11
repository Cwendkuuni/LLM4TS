import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testListPopulationConstructor() {
        assertNotNull(listPopulation);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new Chromosome();
        chromosomes.add(chromosome);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(1, listPopulation.getChromosomes().size());
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidPopulationLimitConstructor() {
        new ListPopulation(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidSetPopulationLimit() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedsLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(chromosomes);
        assertEquals(3, listPopulation.getChromosomes().size());
    }

    @Test
    public void testAddChromosome() {
        listPopulation.addChromosome(new Chromosome());
        assertEquals(1, listPopulation.getChromosomes().size());
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new Chromosome(i));
        }
        listPopulation = new ListPopulation(chromosomes, 5);
        assertEquals(new Chromosome(2), listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new Chromosome(i));
        }
        listPopulation = new ListPopulation(chromosomes, 5);
        listPopulation.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertEquals("[]", listPopulation.toString());
    }

}