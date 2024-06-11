package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityConvergence() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.cumulativeProbability(Math.pow(2, 1024) * 10);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        double p = 0.5;
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(1), 0.001);

        p = 0.1;
        double x = distribution.inverseCumulativeProbability(p);
        assertEquals(Erf.erfInv(p), x, 0.001);

        p = 0.2;
        x = distribution.inverseCumulativeProbability(p);
        assertTrue(x > Erf.erfInv(p));

        p = 0.8;
        x = distribution.inverseCumulativeProbability(p);
        assertTrue(x < Erf.erfInv(p));

        p = 0.999;
        x = distribution.inverseCumulativeProbability(p);
        assertTrue(x > Erf.erfInv(p) - 1 / (2 * Math.sqrt(2 * Math.PI)));
        assertTrue(x < Erf.erfInv(p) + 1 / (2 * Math.sqrt(2 * Math.PI)));
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityConvergence() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(0.5 + 1 / (2 * Math.sqrt(2 * Math.PI) * Math.pow(10, 100)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-1);
    }
}