package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStdDev() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean should be set correctly.", 1.0, dist.getMean(), 0.0);
        assertEquals("Standard deviation should be set correctly.", 2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplWithDefaults() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("Mean should be zero by default.", 0.0, dist.getMean(), 0.0);
        assertEquals("Standard deviation should be one by default.", 1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean.
     */
    @Test
    public void testSettingMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(3.0);
        assertEquals("Mean should be set correctly.", 3.0, dist.getMean(), 0.0);
    }

    /**
     * Test setting standard deviation.
     */
    @Test
    public void testSettingStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(3.0);
        assertEquals("Standard deviation should be set correctly.", 3.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test exception when setting negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.345;
        double expected = 0.6209508287223859;
        double actual = dist.cumulativeProbability(StatUtils.percentile(dist, p));
        assertEquals("Cumulative probability should be correct.", expected, actual, 0.001);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.345;
        double expected = 0.3432429958363953;
        double actual = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability should be correct.", expected, actual, 0.001);
    }

    /**
     * Test inverse cumulative probability with extreme probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityWithExtremeProbabilities() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double pZero = 0.0;
        double pOne = 1.0;
        double expectedZero = Double.NEGATIVE_INFINITY;
        double expectedOne = Double.POSITIVE_INFINITY;
        try {
            dist.inverseCumulativeProbability(pZero);
            fail("Should have thrown MathException for p=0.");
        } catch (MathException e) {
            // Expected
        }
        try {
            dist.inverseCumulativeProbability(pOne);
            fail("Should have thrown MathException for p=1.");
        } catch (MathException e) {
            // Expected
        }
        assertEquals("Inverse cumulative probability for p=0 should be negative infinity.",
                expectedZero, dist.inverseCumulativeProbability(pZero), 0.0);
        assertEquals("Inverse cumulative probability for p=1 should be positive infinity.",
                expectedOne, dist.inverseCumulativeProbability(pOne), 0.0);
    }
}