import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometric.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(5), 0.0001);
        assertEquals(0.6, hypergeometric.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.2142857, hypergeometric.probability(2), 0.0001);
        assertEquals(0.0714286, hypergeometric.probability(1), 0.0001);
        assertEquals(0.4285714, hypergeometric.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.4, hypergeometric.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0714, hypergeometric.upperCumulativeProbability(0), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometric.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, hypergeometric.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hypergeometric.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometric.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(true, hypergeometric.isSupportConnected());
    }

}