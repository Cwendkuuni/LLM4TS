import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);

        assertEquals(listPopulation.getPopulationLimit(), 10);
        assertEquals(listPopulation.getChromosomes().size(), 0);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        assertEquals(listPopulation.getPopulationLimit(), 10);
        assertEquals(listPopulation.getChromosomes().size(), 1);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithExceededChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(20);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        listPopulation.setChromosomes(chromosomes);

        assertEquals(listPopulation.getChromosomes().size(), 1);
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(20);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceededChromosomes() {
        ListPopulation listPopulation = new ListPopulation(1);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(20);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        listPopulation.addChromosomes(chromosomes);

        assertEquals(listPopulation.getChromosomes().size(), 1);
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithExceededChromosomes() {
        ListPopulation listPopulation = new ListPopulation(1);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        assertEquals(listPopulation.getChromosomes(), chromosomes );
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        assertEquals(listPopulation.getChromosomeList(), chromosomes );
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);

        Chromosome chromosome1 = new Chromosome();

        listPopulation.addChromosome(chromosome1);

        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithExceededChromosomes() {
        ListPopulation listPopulation = new ListPopulation(1);

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();

        chromosome1.setFitnessValue(0.6);
        chromosome2.setFitnessValue(0.8);
        chromosome3.setFitnessValue(0.7);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        assertEquals(listPopulation.getFittestChromosome(), chromosome2);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(listPopulation.getPopulationLimit(), 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithExceededChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        assertEquals(listPopulation.getPopulationSize(), 2);
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosomes.add(chromosome1);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        assertEquals(listPopulation.toString(), chromosomes.toString());
    }
}