import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(-1), 0.0);
            assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(11), 0.0);
            assertEquals(0.045454545454545456, hypergeometricDistribution.cumulativeProbability(0), 0.0);
            assertEquals(0.4545454545454546, hypergeometricDistribution.cumulativeProbability(1), 0.0);
            assertEquals(0.9090909090909092, hypergeometricDistribution.cumulativeProbability(2), 0.0);
            assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(3), 0.0);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testProbability() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(0.0, hypergeometricDistribution.probability(-1), 0.0);
            assertEquals(0.0, hypergeometricDistribution.probability(11), 0.0);
            assertEquals(0.045454545454545456, hypergeometricDistribution.probability(0), 0.0);
            assertEquals(0.36363636363636376, hypergeometricDistribution.probability(1), 0.0);
            assertEquals(0.5454545454545455, hypergeometricDistribution.probability(2), 0.0);
            assertEquals(0.0, hypergeometricDistribution.probability(3), 0.0);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(-1), 0.0);
            assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(11), 0.0);
            assertEquals(0.9545454545454546, hypergeometricDistribution.upperCumulativeProbability(0), 0.0);
            assertEquals(0.5, hypergeometricDistribution.upperCumulativeProbability(1), 0.0);
            assertEquals(0.09090909090909091, hypergeometricDistribution.upperCumulativeProbability(2), 0.0);
            assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(3), 0.0);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetNumericalMean() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(1.0, hypergeometricDistribution.getNumericalMean(), 0.0);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetNumericalVariance() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(0.375, hypergeometricDistribution.getNumericalVariance(), 0.0);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetSupportUpperBound() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertEquals(2, hypergeometricDistribution.getSupportUpperBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testIsSupportConnected() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertTrue(hypergeometricDistribution.isSupportConnected());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }
}