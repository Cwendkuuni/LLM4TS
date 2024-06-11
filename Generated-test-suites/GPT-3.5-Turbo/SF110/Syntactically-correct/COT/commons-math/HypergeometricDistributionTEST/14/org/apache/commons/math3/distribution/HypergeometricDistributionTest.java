import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.0476, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        int[] domain = distribution.getDomain(10, 2, 4);
        assertEquals(0, domain[0]);
        assertEquals(2, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        int lowerDomain = distribution.getLowerDomain(10, 2, 4);
        assertEquals(0, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(2, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(4, distribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        int upperDomain = distribution.getUpperDomain(2, 4);
        assertEquals(2, upperDomain);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.0875, distribution.probability(1), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.9524, distribution.upperCumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.4775, distribution.innerCumulativeProbability(0, 1, 1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.8, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.4571, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(1.2, distribution.calculateNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertTrue(distribution.isSupportConnected());
    }
}