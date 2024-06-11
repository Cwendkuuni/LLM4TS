package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.special.Beta;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);
        assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(1.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);

        // Test with 0
        assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0);

        // Test with mean
        double mean = dist.getInitialDomain(0.5);
        assertEquals(mean, dist.cumulativeProbability(mean), 0.0);

        // Test with values greater than mean
        double x = mean + 1.0;
        double p = dist.cumulativeProbability(x);
        assertTrue(p > 0.5);

        // Test with values smaller than mean
        x = mean - 1.0;
        p = dist.cumulativeProbability(x);
        assertTrue(p < 0.5);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);

        // Test with 0
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);

        // Test with 1
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);

        // Test with values in between
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertTrue(x > 0.0);

        // Test with negative values
        p = -1.0;
        try {
            dist.inverseCumulativeProbability(p);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);

        dist.setNumeratorDegreesOfFreedom(2.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);

        dist.setDenominatorDegreesOfFreedom(2.0);
        assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution dist = new FDistributionImpl(1.0, 1.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }
}