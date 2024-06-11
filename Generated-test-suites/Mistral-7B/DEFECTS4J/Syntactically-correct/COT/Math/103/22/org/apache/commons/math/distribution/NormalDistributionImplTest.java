package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Tests for NormalDistributionImpl class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStdDev() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(3.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test default constructor.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testNormalDistributionImplDefault() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean and standard deviation.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testSettingMeanAndStandardDeviation() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(5.0);
        dist.setStandardDeviation(3.0);
        assertEquals(5.0, dist.getMean(), 0.0);
        assertEquals(3.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting negative standard deviation.
     * @throws Exception if an error occurred during testing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulativeProbability method.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double expected = StatUtils.cumulativeProbability(50000, new NormalDistribution(0.0, 1.0), x);
            assertEquals(expected, p, 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method with valid probabilities.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbabilities()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = StatUtils.percentPoint(50000, new NormalDistribution(0.0, 1.0), p);
            assertEquals(expected, x, 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method with invalid probability.
     * @throws Exception if an error occurred during testing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability method with probabilities out of range.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityWithProbabilitiesOutOfRange()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> dist.inverseCumulativeProbability(2.0));
        assertThrows(IllegalArgumentException.class, () -> dist.inverseCumulativeProbability(-1.0));
    }

    /**
     * Test cumulativeProbability method with extreme values.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testCumulativeProbabilityWithExtremeValues() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with extreme probabilities.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityWithExtremeProbabilities()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test cumulativeProbability method with extreme values and large standard
     * deviation.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testCumulativeProbabilityWithExtremeValuesAndLargeStandardDeviation()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 100.0);
        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with extreme probabilities and
     * large standard deviation.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityWithExtremeProbabilitiesAndLargeStandardDeviation()
            throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 100.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test cumulativeProbability method with very large absolute value of x.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testCumulativeProbabilityWithVeryLargeAbsoluteValueOfX() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double x = Double.MAX_VALUE * 10;
        double p = dist.cumulativeProbability(x);
        assertEquals(1.0, p, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with very large absolute value of p.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityWithVeryLargeAbsoluteValueOfP() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 1.0 + 0.01;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
    }

    /**
     * Test convergence of inverseCumulativeProbability method.
     * @throws Exception if an error occurred during testing.
     */
    @Test
    public void testInverseCumulativeProbabilityConvergence() throws Exception {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.51;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.001);

        p = 0.5001;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.001);

        p = 0.50001;
        assertThrows(MaxIterationsExceededException.class, () -> dist.inverseCumulativeProbability(p));
    }
}