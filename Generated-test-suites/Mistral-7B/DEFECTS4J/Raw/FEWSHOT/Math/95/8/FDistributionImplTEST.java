package org.apache.commons.math.distribution;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double prob = distribution.cumulativeProbability(x);
            System.out.printf("%.2f -> %.6f%n", x, prob);
        }
    }

    @Test
    public void testInvalidNumeratorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-3.0, 5.0));
    }

    @Test
    public void testInvalidDenominatorDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(3.0, -5.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = distribution.inverseCumulativeProbability(p);
            System.out.printf("%.2f -> %.6f%n", p, x);
        }
    }

    @Test
    public void testBetaFunction() {
        assertEquals(1.0, Beta.regularizedBeta(0.5 * 3.0, 0.5 * 5.0), 0.0);
        assertEquals(0.0, Beta.regularizedBeta(0.5 * 3.0, 0.5 * 3.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, Beta.regularizedBeta(0.5 * 3.0, -0.5 * 3.0), 0.0);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        double[] pValues = {0.001, 0.1, 0.5, 0.9, 0.999};
        for (double p : pValues) {
            double x = distribution.inverseCumulativeProbability(p);
            System.out.printf("%.4f -> %.6f%n", p, x);
        }
    }
}