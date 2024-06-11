import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        // Test values within domain
        assertEquals(0.9090909090909091, dist.cumulativeProbability(0), 0.000001);
        assertEquals(1.0, dist.cumulativeProbability(1), 0.000001);

        // Test values outside the domain
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.000001);
        assertEquals(1.0, dist.cumulativeProbability(2), 0.000001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        // Test values within domain
        assertEquals(0.45454545454545453, dist.probability(0), 0.000001);
        assertEquals(0.45454545454545453, dist.probability(1), 0.000001);

        // Test values outside the domain
        assertEquals(0.0, dist.probability(-1), 0.000001);
        assertEquals(0.0, dist.probability(2), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        // Test values within domain
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.000001);
        assertEquals(0.9090909090909091, dist.upperCumulativeProbability(1), 0.000001);

        // Test values outside the domain
        assertEquals(0.0, dist.upperCumulativeProbability(-1), 0.000001);
        assertEquals(0.0, dist.upperCumulativeProbability(2), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        assertEquals(1.0, dist.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        assertEquals(0.09714285714285716, dist.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        assertEquals(3, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        assertEquals(2, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        assertTrue(dist.isSupportConnected());
    }
}