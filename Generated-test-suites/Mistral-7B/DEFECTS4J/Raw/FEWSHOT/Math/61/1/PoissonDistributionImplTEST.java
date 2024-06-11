package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.prob.DistributionFunction;
import org.apache.commons.math3.stat.prob.InverseCumulativeDistribution;
import org.junit.Test;

/**
 * Test cases for {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testConstructor() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNegativeMean() throws Exception {
        new PoissonDistributionImpl(-2.5);
    }

    /**
     * Test probability method with various input values.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        DistributionFunction prob = dist.cumulativeProbability();

        // test for non-positive integers and MAX_INT
        for (int i = -5; i <= 6; i++) {
            double p = dist.probability(i);
            if (i >= 0) {
                // check against the cumulative probability distribution
                assertEquals(p, prob.value(i), 1e-8);
            } else {
                // negative integers and MAX_INT should have a probability of zero
                assertEquals(p, 0.0, 1e-8);
            }
        }
    }

    /**
     * Test cumulative probability method with various input values.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        DistributionFunction prob = dist.cumulativeProbability();

        // test for non-positive integers and MAX_INT
        for (int i = -5; i <= 6; i++) {
            double cdf = prob.value(i);
            double p = dist.probability(i);
            assertEquals(cdf, p, 1e-8);
        }

        // test for some specific values
        assertEquals(0.0, prob.value(0), 1e-8);
        assertEquals(0.6826894621016394, prob.value(1), 1e-8);
        assertEquals(0.9772498684177423, prob.value(2), 1e-8);
        assertEquals(0.9993468733573561, prob.value(3), 1e-8);
        assertEquals(1.0, prob.value(4), 1e-8);
    }

    /**
     * Test inverse cumulative probability method with various input probabilities.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        InverseCumulativeDistribution icdf = dist.inverseCumulativeProbability();

        // test for some specific probabilities
        assertEquals(0, icdf.value(0));
        assertEquals(1, icdf.value(0.5));
        assertEquals(1, icdf.value(1));
        assertEquals(2, icdf.value(0.8));
        assertEquals(3, icdf.value(0.95));
        assertEquals(4, icdf.value(0.9975));
        assertEquals(5, icdf.value(0.99997));

        // test for some extreme probabilities
        assertEquals(Integer.MIN_VALUE, icdf.value(0.0));
        assertEquals(Integer.MAX_VALUE, icdf.value(1.0));
    }

    /**
     * Test sampling method with various mean values.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist1 = new PoissonDistributionImpl(1.5);
        PoissonDistribution dist2 = new PoissonDistributionImpl(5.5);
        PoissonDistribution dist3 = new PoissonDistributionImpl(10.5);

        for (int i = 0; i < 1000; i++) {
            int sampledValue1 = dist1.sample();
            int sampledValue2 = dist2.sample();
            int sampledValue3 = dist3.sample();
            assertTrue(sampledValue1 >= 0);
            assertTrue(sampledValue1 <= 1000 * dist1.getMean());
            assertTrue(sampledValue2 >= 0);
            assertTrue(sampledValue2 <= 1000 * dist2.getMean());
            assertTrue(sampledValue3 >= 0);
            assertTrue(sampledValue3 <= 1000 * dist3.getMean());
        }
    }

    /**
     * Test mean, variance, and standard deviation methods.
     *
     * @throws Exception if an error occurs while testing
     */
    @Test
    public void testProperties() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(2.5, dist.getExpectedValue(), 0.0);
        assertEquals(2.5, dist.getVariance(), 0.0);
        assertEquals(1.6128876527632337, new StandardDeviation().evaluate(dist), 1e-8);
    }
}