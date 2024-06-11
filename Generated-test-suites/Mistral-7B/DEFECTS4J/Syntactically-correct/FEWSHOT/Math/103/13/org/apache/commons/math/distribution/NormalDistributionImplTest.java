package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test construction with invalid standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.3413; // value from test in javadoc
        double expected = 0.62095574; // value from javadoc
        assertEquals(expected, dist.cumulativeProbability(p), 0.0001);
    }

    /**
     * Test inverse cumulative probability method with valid parameters.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.8413; // value from test in javadoc
        double expected = 1.28862924; // value from javadoc
        assertEquals(expected, dist.inverseCumulativeProbability(p), 0.0001);
    }

    /**
     * Test inverse cumulative probability method with extreme probabilities.
     */
    @Test
    public void testExtremeInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test inverse cumulative probability method with non-monotonic input.
     */
    @Test(expected = MathException.class)
    public void testNonMonotonicInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(0.5);
    }

    /**
     * Test inverse cumulative probability method with out of bounds input.
     */
    @Test(expected = MathException.class)
    public void testOutOfBoundsInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(2.0);
    }
}