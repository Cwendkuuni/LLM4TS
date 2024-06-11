import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
    }

    @Test
    public void testAddChromosome() {
        listPopulation.addChromosome(new Chromosome());
        assertEquals(1, listPopulation.getChromosomes().size());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        c1.setFitness(10);
        c2.setFitness(20);
        listPopulation.addChromosome(c1);
        listPopulation.addChromosome(c2);
        assertEquals(c2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(-2);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        listPopulation.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithSizeExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithSizeExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(chromosomes);
        listPopulation.addChromosome(new Chromosome());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithSizeExceedingPopulationLimit() {
        for (int i = 0; i < 6; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
    }
}