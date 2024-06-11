import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);

        // Test with values within domain
        assertEquals(0.9984922793176975, distribution.cumulativeProbability(3), 0.0000000001);
        assertEquals(0.996745913535024, distribution.cumulativeProbability(4), 0.0000000001);
        assertEquals(0.9929648241206031, distribution.cumulativeProbability(5), 0.0000000001);

        // Test with values outside of domain
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(11), 0.0);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);

        // Test with values within domain
        assertEquals(0.11164103070869205, distribution.probability(3), 0.0000000001);
        assertEquals(0.16891252955082732, distribution.probability(4), 0.0000000001);
        assertEquals(0.26127527216174194, distribution.probability(5), 0.0000000001);

        // Test with values outside of domain
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(11), 0.0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);

        // Test with values within domain
        assertEquals(0.0072714870600138415, distribution.upperCumulativeProbability(3), 0.0000000001);
        assertEquals(0.0033781323444055, distribution.upperCumulativeProbability(4), 0.0000000001);
        assertEquals(0.0012433391352162516, distribution.upperCumulativeProbability(5), 0.0000000001);

        // Test with values outside of domain
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);
        assertEquals(2.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);
        assertEquals(0.6326530612244898, distribution.getNumericalVariance(), 0.0000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 5, 10);
        assertTrue(distribution.isSupportConnected());
    }
}