import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.16666666666666666, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int[] domain = distribution.getDomain(10, 5, 3);
        assertArrayEquals(new int[]{2, 3}, domain);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }

    @Test
    public void testgetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3333, distribution.probability(1), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.8333, distribution.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.375, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
}