
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionImplTest {
    private final static double DELTA = 1e-15;

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(0, distribution.cumulativeProbability(0), DELTA);
        assertEquals(0.40518481777316, distribution.cumulativeProbability(1), DELTA);
        assertEquals(0.7946740656426411, distribution.cumulativeProbability(2), DELTA);
        assertEquals(0.9161710855704901, distribution.cumulativeProbability(3), DELTA);
        assertEquals(0.9649777431832641, distribution.cumulativeProbability(4), DELTA);
        assertEquals(0.9869119543138417, distribution.cumulativeProbability(5), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(0.4948947889471592, distribution.inverseCumulativeProbability(0.1), DELTA);
        assertEquals(0.9808311759640591, distribution.inverseCumulativeProbability(0.9), DELTA);
        assertEquals(6.388208741334738, distribution.inverseCumulativeProbability(0.999), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), DELTA);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        double lowerBound = distribution.getDomainLowerBound(0.75);
        assertTrue(lowerBound >= 0 && lowerBound < 1);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        double upperBound = distribution.getDomainUpperBound(0.75);
        assertTrue(upperBound > 100);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        double initialDomain = distribution.getInitialDomain(0.75);
        assertTrue(initialDomain > 0 && initialDomain < 1);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        distribution.setNumeratorDegreesOfFreedom(4);

        assertEquals(4, distribution.getNumeratorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);
        distribution.setDenominatorDegreesOfFreedom(6);

        assertEquals(6, distribution.getDenominatorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 5);

        assertEquals(5, distribution.getDenominatorDegreesOfFreedom(), DELTA);
    }
}
