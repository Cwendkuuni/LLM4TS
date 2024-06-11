import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution hypergeometricDistribution;

    @Before
    public void setUp() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testNumberOfSuccessesNegative() {
        new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testSampleSizeNegative() {
        new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 3);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.4, hypergeometricDistribution.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.001);
    }

    // Additional test cases for other methods

}