import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.5, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.2143, distribution.probability(1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, distribution.getNumericalVariance(), 0.0001);
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