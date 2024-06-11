package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.descript.Statistics;
import org.junit.Test;

/**
 * Test cases for FDistributionImpl class
 */
public class FDistributionImplTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        assertEquals(FDistType.F2, dist.getDegreesOfFreedom1());
        assertEquals(FDistType.F3, dist.getDegreesOfFreedom2());
    }

    /**
     * Test cumulativeProbability method
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            assertEquals(x, dist.inverseCumulativeProbability(p), 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method with valid input
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = dist.inverseCumulativeProbability(p);
            assertEquals(p, dist.cumulativeProbability(x), 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method with invalid input (p < 0)
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability method with invalid input (p > 1)
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityGreaterOneProbability() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test getDomainLowerBound method
     */
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        double p = 0.1;
        assertEquals(0.0, dist.getDomainLowerBound(p), 0.001);
    }

    /**
     * Test getDomainUpperBound method
     */
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        double p = 0.9;
        assertEquals(Double.POSITIVE_INFINITY, dist.getDomainUpperBound(p), 0.001);
    }

    /**
     * Test getInitialDomain method
     */
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        assertEquals(3.0 / (3.0 - 2.0), dist.getInitialDomain(p), 0.001);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, dist.getNumeratorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test setNumeratorDegreesOfFreedom with negative value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegativeValue() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom with negative value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegativeValue() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }

    /**
     * Test getting numerator and denominator degrees of freedom
     */
    @Test
    public void testGetDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test statistics method
     */
    @Test
    public void testStatistics() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 3.0);
        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(Statistics.uniform(0.0, 1.0).sample());
        }
        double mean = Statistics.mean(values);
        double stdev = Statistics.standardDeviation(values);
        assertEquals(3.0 / (3.0 - 2.0), mean, 0.01);
        assertEquals(Math.sqrt(3.0 * 2.0 / (3.0 - 2.0) / (3.0 - 2.0 - 1.0)), stdev, 0.01);
    }
}