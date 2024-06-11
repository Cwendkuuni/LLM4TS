import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution dist = new FDistribution(1.0, 2.0);
            assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
            assertFalse(dist.isSupportLowerBoundInclusive());
            assertFalse(dist.isSupportUpperBoundInclusive());
            assertFalse(dist.isSupportConnected());
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double density = dist.density(x);
            System.out.printf("density at x=%.2f = %.8f%n", x, density);
            assertTrue(density >= 0.0);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double prob = dist.cumulativeProbability(x);
            System.out.printf("cumulativeProbability at x=%.2f = %.8f%n", x, prob);
            assertTrue(prob >= 0.0 && prob <= 1.0);
        }
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double mean = dist.getNumericalMean();
        System.out.printf("Numerical mean: %.8f%n", mean);
        assertEquals(1.5, mean, 0.01);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFDistributionVariance() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        dist.getNumericalVariance();
    }

    @Test
    public void testFDistributionCalculateNumericalVariance() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        dist.calculateNumericalVariance();
        assertTrue(Double.isNaN(dist.getNumericalVariance()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        dist.getSolverAbsoluteAccuracy();
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testFDistributionSupportInclusive() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertTrue(dist.isSupportConnected());
    }
}