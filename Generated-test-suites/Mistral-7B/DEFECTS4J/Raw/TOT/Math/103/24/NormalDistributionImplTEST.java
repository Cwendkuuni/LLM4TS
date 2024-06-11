package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl()}.
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImplWithParameters() {
        double mean = 2.5;
        double standardDeviation = 3.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(standardDeviation, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     * This test checks the correctness of the cumulative probability function
     * for various input values. It also checks the edge cases where the
     * argument is far from the mean.
     */
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double[] testData = {-5.0, -2.0, -1.0, 0.0, 0.5, 1.0, 2.0, 3.0, 5.0};

        for (double x : testData) {
            double p = distribution.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + Erf.erf((x - distribution.getMean()) /
                    (distribution.getStandardDeviation() * Math.sqrt(2.0))));
            assertEquals(expected, p, 1e-7);
        }

        // Test edge cases:
        double lowerBound = -20.0 * distribution.getStandardDeviation();
        double upperBound = 20.0 * distribution.getStandardDeviation();
        double lowerProbability = distribution.cumulativeProbability(lowerBound);
        double upperProbability = distribution.cumulativeProbability(upperBound);
        assertEquals(0.0, lowerProbability, 1e-7);
        assertEquals(1.0, upperProbability, 1e-7);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the correctness of the inverse cumulative probability
     * function for various input probabilities.
     */
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double[] testProbabilities = {0.0, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99, 0.999};
        double[] expectedValues = {
            Double.NEGATIVE_INFINITY, -1.282111662512796, -0.674489753460542,
            0.0, 0.0, 0.674489753460544, 1.282111662512795, 3.05355555395363,
            6.0
        };

        for (int i = 0; i < testProbabilities.length; i++) {
            double probability = testProbabilities[i];
            double expectedValue = expectedValues[i];
            double actualValue = 0.0;

            try {
                actualValue = distribution.inverseCumulativeProbability(probability);
            } catch (MathException e) {
                // Ignore, as the test case for p=0 and p=1 already covers this situation
            }

            assertEquals(expectedValue, actualValue, 1e-7);
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#setStandardDeviation(double)}.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#setMean(double)}.
     */
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#getMean()}.
     */
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);

        // Test setting the mean and checking it
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#getStandardDeviation()}.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);

        // Test setting the standard deviation and checking it
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the behavior of the inverse cumulative probability
     * function when the probability is out of the valid range (0.0 to 1.0).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     * This test checks the behavior of the cumulative probability function
     * when the argument is out of the valid range.
     */
    @Test(expected = MathException.class)
    public void testInvalidArgument() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.cumulativeProbability(Double.NaN);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the behavior of the inverse cumulative probability
     * function when the probability is too close to 0.0 or 1.0.
     */
    @Test
    public void testProbabilityTooCloseToEdge() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double lowerBoundProbability = 1e-12;
        double upperBoundProbability = 1.0 - 1e-12;
        double lowerBoundValue = distribution.inverseCumulativeProbability(lowerBoundProbability);
        double upperBoundValue = distribution.inverseCumulativeProbability(upperBoundProbability);
        assertEquals(Precision.isClose(distribution.getMean(), lowerBoundValue, 1e-6), true);
        assertEquals(Precision.isClose(distribution.getMean(), upperBoundValue, 1e-6), true);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the behavior of the inverse cumulative probability
     * function when the distribution has a very small standard deviation.
     */
    @Test
    public void testSmallStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(1e-12);
        double probability = 0.5;
        double value = distribution.inverseCumulativeProbability(probability);
        assertEquals(distribution.getMean(), value, 1e-14);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     * This test checks the behavior of the cumulative probability function
     * when the random number generator returns an out-of-range value.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = Double.MAX_VALUE;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     * This test checks the behavior of the cumulative probability function
     * when the random number generator returns a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = -1.0;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the behavior of the inverse cumulative probability
     * function when the probability is exactly 0.0 or 1.0.
     */
    @Test
    public void testProbabilityZeroOrOne() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double lowerBoundProbability = 0.0;
        double upperBoundProbability = 1.0;
        double lowerBoundValue = distribution.inverseCumulativeProbability(lowerBoundProbability);
        double upperBoundValue = distribution.inverseCumulativeProbability(upperBoundProbability);
        assertEquals(Double.NEGATIVE_INFINITY, lowerBoundValue, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, upperBoundValue, 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     * This test checks the behavior of the inverse cumulative probability
     * function when the probability is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-0.1);
    }
}