package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * Test cases for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test construction with valid mean.
     */
    @Test
    public void testConstruction() {
        PoissonDistribution poisson = new PoissonDistributionImpl(1.5);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        assertEquals("Mean is not set correctly", 1.5, poissonImpl.getMean(), 1e-6);
        assertEquals("Normal distribution is not initialized", true, poissonImpl.normal != null);
    }

    /**
     * Test construction with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbability() {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        double prob;

        prob = poissonImpl.probability(0);
        assertEquals("Probability of 0 is not correct", 0.0738, prob, 1e-5);

        prob = poissonImpl.probability(1);
        assertEquals("Probability of 1 is not correct", 0.1537, prob, 1e-5);

        prob = poissonImpl.probability(2);
        assertEquals("Probability of 2 is not correct", 0.1855, prob, 1e-5);

        prob = poissonImpl.probability(Integer.MAX_VALUE);
        assertEquals("Probability of max int is not correct", 0.0, prob, 1e-5);
    }

    /**
     * Test cumulative probability method with valid input.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        double cumProb;

        cumProb = poissonImpl.cumulativeProbability(0);
        assertEquals("Cumulative probability of 0 is not correct", 0.0, cumProb, 1e-5);

        cumProb = poissonImpl.cumulativeProbability(1);
        assertEquals("Cumulative probability of 1 is not correct", 0.1537, cumProb, 1e-5);

        cumProb = poissonImpl.cumulativeProbability(2);
        assertEquals("Cumulative probability of 2 is not correct", 0.3294, cumProb, 1e-5);

        cumProb = poissonImpl.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals("Cumulative probability of max int is not correct", 1.0, cumProb, 1e-5);
    }

    /**
     * Test normal approximation method with valid input.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        double normProb;

        normProb = poissonImpl.normalApproximateProbability(0);
        assertEquals("Normal approximation probability of 0 is not correct", 0.0740, normProb, 1e-5);

        normProb = poissonImpl.normalApproximateProbability(1);
        assertEquals("Normal approximation probability of 1 is not correct", 0.1536, normProb, 1e-5);

        normProb = poissonImpl.normalApproximateProbability(2);
        assertEquals("Normal approximation probability of 2 is not correct", 0.1854, normProb, 1e-5);

        normProb = poissonImpl.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals("Normal approximation probability of max int is not correct", 0.0, normProb, 1e-5);
    }

    /**
     * Test sample method with valid mean.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        int sample;

        sample = poisson.sample();

        assertTrue("Sample is not within the bounds", sample >= 0 && sample <= 1000 * poisson.getMean());
        assertEquals("Sample does not follow the distribution", poisson.cumulativeProbability(sample), 1.0, 1e-5);
    }

    /**
     * Test inverse cumulative probability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        double p;
        int x;

        p = 0.2;
        x = poissonImpl.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability for small p is not correct", 0, x);

        p = 0.5;
        x = poissonImpl
                .inverseCumulativeProbability(p); // should be around 1
        assertTrue("Inverse cumulative probability for p = 0.5 is not close to max value", x >= 999 && x <= 1001);

        p = 0.999;
        x = poissonImpl.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability for large p is not close to max value", 1000, x);
    }

    /**
     * Test inverse cumulative probability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability() throws MathException {
        PoissonDistribution poisson = new PoissonDistributionImpl(3.0);
        PoissonDistributionImpl poissonImpl = (PoissonDistributionImpl) poisson;
        double p;

        p = 1.1;
        poissonImpl.inverseCumulativeProbability(p);
    }

    /**
     * Test method getDomainLowerBound
     */
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poissonImpl = new PoissonDistributionImpl(1.0);
        int bound = poissonImpl.getDomainLowerBound(0.0);
        assertEquals("Lower bound is not correct", 0, bound);
    }

    /**
     * Test method getDomainUpperBound
     */
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poissonImpl = new PoissonDistributionImpl(1.0);
        int bound = poissonImpl.getDomainUpperBound(0.0);
        assertEquals("Upper bound is not correct", Integer.MAX_VALUE, bound);
    }
}