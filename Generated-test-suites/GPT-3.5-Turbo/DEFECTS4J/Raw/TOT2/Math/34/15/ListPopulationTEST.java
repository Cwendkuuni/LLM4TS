import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        // Test for valid population limit
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        
        // Test for invalid population limit
        try {
            listPopulation = new ListPopulation(0);
            fail("Exception not thrown for non-positive population limit");
        } catch (NotPositiveException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome());
        chromosomeList.add(new Chromosome());

        // Test for adding chromosomes within the limit
        listPopulation.addChromosomes(chromosomeList);
        assertEquals(2, listPopulation.getPopulationSize());

        // Test for adding chromosomes exceeding the limit
        try {
            listPopulation.addChromosomes(chromosomeList);
            fail("Exception not thrown for too many chromosomes added");
        } catch (NumberIsTooLargeException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(2);
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        // Test adding one chromosome
        listPopulation.addChromosome(chromosome1);
        assertEquals(1, listPopulation.getPopulationSize());

        // Test adding additional chromosome
        listPopulation.addChromosome(chromosome2);
        assertEquals(2, listPopulation.getPopulationSize());

        // Test adding chromosome when population is full
        try {
            listPopulation.addChromosome(new Chromosome());
            fail("Exception not thrown for adding chromosome when population is full");
        } catch (NumberIsTooLargeException e) {
            // Exception expected
        }
    }

    // Additional test cases for other public methods can be added here

}