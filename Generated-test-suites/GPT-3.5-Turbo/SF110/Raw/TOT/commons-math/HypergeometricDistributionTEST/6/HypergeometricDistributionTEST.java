import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        // Typical use case
        HypergeometricDistribution dist1 = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist1.getPopulationSize());
        assertEquals(5, dist1.getNumberOfSuccesses());
        assertEquals(3, dist1.getSampleSize());

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertEquals(1, dist2.getPopulationSize());
        assertEquals(0, dist2.getNumberOfSuccesses());
        assertEquals(0, dist2.getSampleSize());

        // Error scenario - Negative values
        try {
            HypergeometricDistribution dist3 = new HypergeometricDistribution(-10, -5, -3);
            fail("Should have thrown an exception");
        } catch (NotPositiveException e) {
            // Exception thrown correctly
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case - x is within the domain
        assertEquals(0.26666, dist.cumulativeProbability(1), 0.00001);

        // Edge case - x is at the lower bound
        assertEquals(0.13333, dist.cumulativeProbability(0), 0.00001);

        // Edge case - x is at the upper bound
        assertEquals(1.0, dist.cumulativeProbability(2), 0.00001);

        // Error scenario - x is outside the domain
        try {
            dist.cumulativeProbability(3);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // Exception thrown correctly
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case - x is within the domain
        assertEquals(0.33333, dist.probability(1), 0.00001);

        // Edge case - x is at the lower bound
        assertEquals(0.2, dist.probability(0), 0.00001);

        // Edge case - x is at the upper bound
        assertEquals(0.33333, dist.probability(2), 0.00001);

        // Error scenario - x is outside the domain
        assertEquals(0.0, dist.probability(3), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case - x is within the domain
        assertEquals(0.73333, dist.upperCumulativeProbability(1), 0.00001);

        // Edge case - x is at the lower bound
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.00001);

        // Edge case - x is at the upper bound
        assertEquals(0.26666, dist.upperCumulativeProbability(2), 0.00001);

        // Error scenario - x is outside the domain
        try {
            dist.upperCumulativeProbability(3);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // Exception thrown correctly
        }
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(1.5, dist.getNumericalMean(), 0.00001);

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertEquals(0.0, dist2.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0.642857, dist.getNumericalVariance(), 0.00001);

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertEquals(0.0, dist2.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0, dist.getSupportLowerBound());

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertEquals(0, dist2.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(3, dist.getSupportUpperBound());

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertEquals(0, dist2.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertTrue(dist.isSupportConnected());

        // Edge case - minimum values
        HypergeometricDistribution dist2 = new HypergeometricDistribution(1, 0, 0);
        assertTrue(dist2.isSupportConnected());
    }

}