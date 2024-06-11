package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean should be set correctly.", 1.0, distribution.getMean(), 0.0);
        assertEquals("Standard deviation should be set correctly.", 2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplConstructorWithDefaultValues() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals("Mean should be set to default value 0.", 0.0, distribution.getMean(), 0.0);
        assertEquals("Standard deviation should be set to default value 1.", 1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean should be gettable.", 1.0, distribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanWithNegativeValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(-1.0);
    }

    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals("Mean should be set correctly.", 3.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Standard deviation should be gettable.", 2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithZeroOrNegativeValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals("Standard deviation should be set correctly.", 3.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithinThreeStandardDeviations() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = distribution.cumulativeProbability(0.5);
        double expectedProbability = Erf.erf(0.5) / 2.0;
        assertEquals("Probability should be correct.", expectedProbability, probability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutsideThreeStandardDeviations() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double x = 10.0; // Ten standard deviations away from mean
        distribution.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbabilityWithValidProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double expectedValue = 0.0;
        double computedValue = distribution.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability should be correct.", expectedValue, computedValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 1.1;
        distribution.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithProbabilityOutsideRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 1.01;
        distribution.inverseCumulativeProbability(p);
    }
}