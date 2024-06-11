import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.073, dist1.cumulativeProbability(0), 0.001);

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(0.525, dist2.cumulativeProbability(10), 0.001);
    }

    @Test
    public void testProbability() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.692, dist1.probability(2), 0.001);

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(0.102, dist2.probability(10), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.538, dist1.upperCumulativeProbability(2), 0.001);

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(0.875, dist2.upperCumulativeProbability(10), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist1.getNumericalMean(), 0.001);

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(6.0, dist2.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.45, dist1.getNumericalVariance(), 0.001);

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(1.276, dist2.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist1.getSupportLowerBound());

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(10, dist2.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        // Test case 1
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist1.getSupportUpperBound());

        // Test case 2
        HypergeometricDistribution dist2 = new HypergeometricDistribution(100, 30, 20);
        assertEquals(20, dist2.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}