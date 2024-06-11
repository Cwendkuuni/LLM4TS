import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    @Test
    public void testConstructor1() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testConstructor2() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(2, population.getChromosomes().size());
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesConstructor() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidPopulationLimitConstructor() {
        ListPopulation population = new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManyChromosomesConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 2);
    }

    // Add more test cases for other methods in ListPopulation

}