package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_ValidConstruction() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImpl_InvalidConstruction_NegativeDOFs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FDistributionImpl(-5.0, 10.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FDistributionImpl(5.0, -10.0);
        });
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);

        // Test for edge cases
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0);
        assertEquals(1.0, fd.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);

        // Test for some values in between
        double x = 1.5;
        double p = fd.cumulativeProbability(x);
        assertEquals(Beta.regularizedBeta(7.5 * x / (15.0 + 7.5 * x), 2.5, 2.5), p, 0.001);

        x = 2.5;
        p = fd.cumulativeProbability(x);
        assertEquals(Beta.regularizedBeta(10.0 * x / (15.0 + 10.0 * x), 2.5, 2.5), p, 0.001);

        x = 4.0;
        p = fd.cumulativeProbability(x);
        assertEquals(Beta.regularizedBeta(20.0 * x / (15.0 + 20.0 * x), 2.5, 2.5), p, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability_ValidValues() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);

        // Test for edge cases
        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0);

        // Test for some values in between
        double p = 0.25;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(Beta.regularizedBetaInverse(p, 2.5, 2.5), x, 0.001);

        p = 0.75;
        x = fd.inverseCumulativeProbability(p);
        assertEquals(Beta.regularizedBetaInverse(p, 2.5, 2.5), x, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability_InvalidValues() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);

        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(1.1));
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(0.0));
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(1.0));
        assertThrows(IllegalArgumentException.class, () -> fd.inverseCumulativeProbability(-1.0));
    }
}