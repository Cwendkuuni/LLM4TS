import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor1() {
        // Given
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.8);

        // Then
        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructor2() {
        // Given
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        // Then
        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        // Given
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.8);

        // When
        Population nextGeneration = elitisticListPopulation.nextGeneration();

        // Then
        assertNotNull(nextGeneration);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeLow() {
        // Given
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        // When
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeHigh() {
        // Given
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        // When
        elitisticListPopulation.setElitismRate(1.1);
    }
}