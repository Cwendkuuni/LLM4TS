import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {

        try {
            new HypergeometricDistribution(0, 5, 2);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Population size must be positive.", e.getMessage());
        }

        try {
            new HypergeometricDistribution(-1, 5, 2);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Population size must be positive.", e.getMessage());
        }

        try {
            new HypergeometricDistribution(10, -1, 2);
        } catch (NotPositiveException e) {
            assertEquals("Number of successes must be non-negative.", e.getMessage());
        }

        try {
            new HypergeometricDistribution(10, 5, -1);
        } catch (NotPositiveException e) {
            assertEquals("Number of samples must be non-negative.", e.getMessage());
        }

        try {
            new HypergeometricDistribution(10, 15, 2);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Number of successes cannot be greater than population size.", e.getMessage());
        }

        try {
            new HypergeometricDistribution(10, 5, 15);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Number of samples cannot be greater than population size.", e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.4167, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.8333, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(0.0278, dist.probability(1), 0.0001);
        assertEquals(0.2222, dist.probability(2), 0.0001);
        assertEquals(0.5, dist.probability(3), 0.0001);
        assertEquals(0.25, dist.probability(4), 0.0001);
        assertEquals(0.0, dist.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.5833, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.1667, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.421875, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}