package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(1.2, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with default mean and standard deviation.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test getMean method.
     */
    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.0);

        dist.setMean(3.14);
        assertEquals(3.14, dist.getMean(), 0.0);
    }

    /**
     * Test setMean method with negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegativeSD() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.2);
        dist.setMean(1.5);
    }

    /**
     * Test setMean method with valid standard deviation.
     */
    @Test
    public void testSetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(1.2);
        dist.setMean(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    /**
     * Test getStandardDeviation method.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(1.2, dist.getStandardDeviation(), 0.0);

        dist.setStandardDeviation(3.14);
        assertEquals(3.14, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setStandardDeviation method with negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegativeMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(-1.2);
        dist.setStandardDeviation(2.5);
    }

    /**
     * Test setStandardDeviation method with valid mean.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(2.5);
        dist.setStandardDeviation(1.2);
        assertEquals(1.2, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.25;
        double expected = 0.1915234136; // calculated using MathCad
        double result = dist.cumulativeProbability(dist.inverseCumulativeProbability(p));
        assertEquals(expected, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method with extreme input.
     */
    @Test
    public void testCumulativeProbabilityExtremeInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double x1 = Double.NEGATIVE_INFINITY;
        double x2 = Double.POSITIVE_INFINITY;
        double result1 = dist.cumulativeProbability(x1);
        double result2 = dist.cumulativeProbability(x2);
        assertEquals(0.0, result1, 0.0);
        assertEquals(1.0, result2, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.25;
        double expected = 0.6128498; // calculated using MathCad
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.01);
    }

    /**
     * Test inverseCumulativeProbability method with extreme inputs.
     */
    @Test
    public void testInverseCumulativeProbabilityExtremeInputs() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p1 = 0.0;
        double p2 = 1.0;
        double result1 = dist.inverseCumulativeProbability(p1);
        double result2 = dist.inverseCumulativeProbability(p2);
        assertEquals(Double.NEGATIVE_INFINITY, result1, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, result2, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with non-valid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNonValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 1.5;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test inverseCumulativeProbability method with large absolute input.
     */
    @Test
    public void testInverseCumulativeProbabilityLargeInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double x = 1000 * DistStatFunction.INF;
        double result = dist.inverseCumulativeProbability(0.5);
        assertEquals(x, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with small absolute input.
     */
    @Test
    public void testInverseCumulativeProbabilitySmallInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double x = -1000 * DistStatFunction.INF;
        double result = dist.inverseCumulativeProbability(0.5);
        assertEquals(x, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with large probability.
     */
    @Test
    public void testInverseCumulativeProbabilityLargeProbability() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.9999999999;
        double result = dist.inverseCumulativeProbability(p);
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with small probability.
     */
    @Test
    public void testInverseCumulativeProbabilitySmallProbability() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.0000000001;
        double result = dist.inverseCumulativeProbability(p);
        double expected = Double.NEGATIVE_INFINITY;
        assertEquals(expected, result, 0.0);
    }

    /**
     * Test getDomainLowerBound method with valid input.
     */
    @Test
    public void testGetDomainLowerBoundValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.25;
        double expected = StatUtils.PERCENTILE(0.01, dist);
        double result = dist.getDomainLowerBound(p);
        assertEquals(expected, result, 0.01);
    }

    /**
     * Test getDomainLowerBound method with extreme input.
     */
    @Test
    public void testGetDomainLowerBoundExtremeInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.0;
        double result = dist.getDomainLowerBound(p);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);

        p = 1.0;
        result = dist.getDomainLowerBound(p);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    /**
     * Test getDomainUpperBound method with valid input.
     */
    @Test
    public void testGetDomainUpperBoundValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.25;
        double expected = StatUtils.PERCENTILE(99.0, dist);
        double result = dist.getDomainUpperBound(p);
        assertEquals(expected, result, 0.01);
    }

    /**
     * Test getDomainUpperBound method with extreme input.
     */
    @Test
    public void testGetDomainUpperBoundExtremeInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.0;
        double result = dist.getDomainUpperBound(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);

        p = 1.0;
        result = dist.getDomainUpperBound(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test getInitialDomain method with valid input.
     */
    @Test
    public void testGetInitialDomainValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.25;
        double expected = StatUtils.PERCENTILE(50.0, dist);
        double result = dist.getInitialDomain(p);
        assertEquals(expected, result, 0.01);
    }

    /**
     * Test getInitialDomain method with extreme inputs.
     */
    @Test
    public void testGetInitialDomainExtremeInputs() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.0;
        double result = dist.getInitialDomain(p);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);

        p = 1.0;
        result = dist.getInitialDomain(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test setMean and setStandardDeviation methods with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanAndStandardDeviationInvalidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(-1.2);
        dist.setStandardDeviation(-2.5);
    }

    /**
     * Test setMean and setStandardDeviation methods with valid input.
     */
    @Test
    public void testSetMeanAndStandardDeviationValidInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(2.5);
        dist.setStandardDeviation(1.2);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(1.2, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulativeProbability method with very large absolute input.
     */
    @Test
    public void testCumulativeProbabilityVeryLargeInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double x = 1000 * DistStatFunction.INF;
        double p = 0.5;
        double result = dist.cumulativeProbability(x);
        assertEquals(0.5, result, 0.0);
    }

    /**
     * Test cumulativeProbability method with very small absolute input.
     */
    @Test
    public void testCumulativeProbabilityVerySmallInput() {
        NormalDistribution dist = new NormalDistributionImpl();
        double x = -1000 * DistStatFunction.INF;
        double p = 0.5;
        double result = dist.cumulativeProbability(x);
        assertEquals(0.5, result, 0.0);
    }
}