
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);

        double x = 0;
        double expected = 0;
        double delta = 0.0001;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);

        x = 1;
        expected = 0.3529;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);

        x = 2;
        expected = 0.65476;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);

        x = 3;
        expected = 0.82513;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);

        x = 4;
        expected = 0.89888;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);

        x = 5;
        expected = 0.93736;
        assertEquals(expected, distribution.cumulativeProbability(x), delta);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);

        double p = 0;
        double expected = 0;
        double delta = 0.001;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);

        p = 0.5;
        expected = 1.928;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);

        p = 0.8;
        expected = 5.574;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);

        p = 0.95;
        expected = 13.119;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);

        p = 0.99;
        expected = 54.753;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);

        p = 1;
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), delta);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);

        double p = 0;
        double expected = 0;
        double delta = 0.001;
        assertEquals(expected, distribution.getDomainLowerBound(p), delta);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);

        double p = 0;
        double expected = Double.MAX_VALUE;
        double delta = 0.001;
        assertEquals(expected, distribution.getDomainUpperBound(p), delta);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);

        double p = 0.5;
        double expected = 1.5;
        double delta = 0.001;
        assertEquals(expected, distribution.getInitialDomain(p), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_positiveValue() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setNumeratorDegreesOfFreedom(4);
        assertEquals(4, distribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_negativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_positiveValue() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setDenominatorDegreesOfFreedom(5);
        assertEquals(5, distribution.getDenominatorDegreesOfFreedom(), 0);
    }
}
