import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.3333, hypergeometric.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.2308, hypergeometric.probability(2), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.6923, hypergeometric.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.5, hypergeometric.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.3820, hypergeometric.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0, hypergeometric.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, hypergeometric.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 3, 5);
        assertEquals(true, hypergeometric.isSupportConnected());
    }
}