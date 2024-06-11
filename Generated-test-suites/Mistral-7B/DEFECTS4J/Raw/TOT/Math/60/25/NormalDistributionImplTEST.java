package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
        assertEquals(NormalDistribution.TYPE_STANDARD_NORMAL, dist.getType(), 0);
    }

    /**
     * Tests the density method.
     */
    @Test
    public void testDensity() {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        final double x = 0.5;
        final double expected = 0.127454556345235; // computed using Maple
        assertEquals(expected, dist.density(x), 0.0000000001);
    }

    /**
     * Tests the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        final double x = 0.5;
        final double p = dist.cumulativeProbability(x);
        final double expected = 0.682689492137; // computed using Maple
        assertEquals(expected, p, 0.0000000001);

        // test extreme values
        final double xMinusInf = FastMath.nextDown(Float.MIN_VALUE);
        final double xPlusInf = FastMath.nextUp(Float.MAX_VALUE);
        assertEquals(0.0, dist.cumulativeProbability(xMinusInf), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(xPlusInf), 0.0);
    }

    /**
     * Tests the inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        // test extreme values
        final double pMinusInf = FastMath.nextDown(0.0);
        final double pPlusInf = FastMath.nextUp(1.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(pMinusInf), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(pPlusInf), 0.0);

        // test normal values
        final double p = 0.3413;
        final double x = dist.inverseCumulativeProbability(p);
        final double expected = -0.367347975314; // computed using Maple
        assertEquals(expected, x, 0.0000000001);
    }

    /**
     * Tests the sample method.
     */
    @Test
    public void testSample() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        RandomGenerator rg = new org.apache.commons.math3.random.MersenneTwisterFastRandom();
        for (int i = 0; i < 10; i++) {
            final double x = dist.sample(rg);
            final double expected = Erf.erfInv(2 * Math.random() - 1) * sd + mean;
            assertEquals(expected, x, 0.01);
        }
    }
}