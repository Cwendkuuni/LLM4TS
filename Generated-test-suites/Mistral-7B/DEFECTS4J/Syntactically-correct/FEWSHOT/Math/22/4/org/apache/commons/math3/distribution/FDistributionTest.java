package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeDOFs() {
        FDistribution dist = new FDistribution(-1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationZeroDOFs() {
        FDistribution dist = new FDistribution(0.0, 1.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(0.3989422804010993, dist.density(1.0), 0.000001);
        assertEquals(0.0, dist.density(2.0), 0.000001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(0.156667302839557, dist.cumulativeProbability(0.5), 0.000001);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.000001);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double variance = dist.getNumericalVariance();
        assertTrue(variance > 0.0);
        assertEquals(0.5, variance, 0.01);

        dist = new FDistribution(2.0, 2.0);
        variance = dist.getNumericalVariance();
        assertEquals(0.5, variance, 0.01);

        dist = new FDistribution(1.0, 1.0);
        assertNull(dist.getNumericalVariance());
    }

    @Test
    public void testFDistributionRandomValues() {
        FDistribution dist = new FDistribution(1.0, 2.0, new Random());
        for (int i = 0; i < 1000; i++) {
            double value = dist.inverseCumulativeProbability(new Double(Math.random()).doubleValue());
            assertTrue(value >= 0.0);
        }
    }
}