import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        try {
            ListPopulation lp = new ListPopulation(-1); // NotPositiveException should be thrown
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation lp = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        lp.setChromosomes(chromosomes);

        assertEquals(chromosomes, lp.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation lp = new ListPopulation(1);
        Chromosome chromo1 = new Chromosome();
        Chromosome chromo2 = new Chromosome();

        lp.addChromosome(chromo1);
        try {
            lp.addChromosome(chromo2); // NumberIsTooLargeException should be thrown
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation lp = new ListPopulation(5);
        lp.addChromosome(new Chromosome(5));
        lp.addChromosome(new Chromosome(10));

        assertEquals(10, lp.getFittestChromosome().getFitness());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation lp = new ListPopulation(5);
        lp.setPopulationLimit(10);

        assertEquals(10, lp.getPopulationLimit());
    }

    @Test
    public void testPopulationSize() {
        ListPopulation lp = new ListPopulation(5);
        lp.addChromosome(new Chromosome());
        lp.addChromosome(new Chromosome());

        assertEquals(2, lp.getPopulationSize());
    }

}