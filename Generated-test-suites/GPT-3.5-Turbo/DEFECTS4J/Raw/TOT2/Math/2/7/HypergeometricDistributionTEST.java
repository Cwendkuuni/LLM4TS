import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_PositiveParameters() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertNotNull(hypergeometric);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_PopulationSizeZero() {
        new HypergeometricDistribution(0, 2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -2, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(5, 6, 3);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.619, hypergeometric.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.047, hypergeometric.upperCumulativeProbability(1), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.428, hypergeometric.probability(1), 0.001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.6, hypergeometric.getNumericalMean(), 0.1);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.18, hypergeometric.getNumericalVariance(), 0.01);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0, hypergeometric.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertEquals(2, hypergeometric.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 2, 3);
        assertTrue(hypergeometric.isSupportConnected());
    }
}