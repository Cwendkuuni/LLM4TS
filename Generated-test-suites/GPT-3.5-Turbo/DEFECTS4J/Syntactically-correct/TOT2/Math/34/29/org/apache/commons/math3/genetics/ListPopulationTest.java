import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("chromosome1"));
        chromosomes.add(new Chromosome("chromosome2"));

        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(2);
        listPopulation.addChromosome(new Chromosome("chromosome1"));
        listPopulation.addChromosome(new Chromosome("chromosome2"));

        assertEquals(2, listPopulation.getPopulationSize());
        try {
            listPopulation.addChromosome(new Chromosome("chromosome3"));
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosome(new Chromosome("chromosome1"));
        listPopulation.addChromosome(new Chromosome("chromosome2"));

        Chromosome fittest = listPopulation.getFittestChromosome();
        assertEquals("chromosome2", fittest.toString());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);

        try {
            listPopulation.setPopulationLimit(4);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("chromosome1"));
        chromosomes.add(new Chromosome("chromosome2"));

        listPopulation.addChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
    }

}