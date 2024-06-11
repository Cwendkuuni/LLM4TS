package org.apache.commons.math.distribution;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

class NormalDistributionImplTest {

    @Test
    void testNormalDistributionImplConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean());
        assertEquals(1.0, dist.getStandardDeviation());
    }

    @Test
    void testNormalDistributionImplMeanAndStandardDeviationSetters() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean());
        assertEquals(2.0, dist.getStandardDeviation());
    }

    @Test
    void testNormalDistributionImplWithNegativeStandardDeviation() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0.0, -1.0));
    }

    @Test
    void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.3989422804010993;
        assertEquals(expected, dist.density(0.0), 0.00000001);
    }

    @Test
    void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(0.0), 0.00001);

        expected = 0.1587;
        assertEquals(expected, dist.cumulativeProbability(1.0), 0.0001);

        expected = 0.8413;
        assertEquals(expected, dist.cumulativeProbability(-1.0), 0.0001);

        expected = 0.0;
        assertEquals(expected, dist.cumulativeProbability(-3.0), 0.0);

        expected = 1.0;
        assertEquals(expected, dist.cumulativeProbability(3.0), 0.0);
    }

    @Test
    void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.0;
        assertEquals(expected, dist.inverseCumulativeProbability(0.0), 0.0001);

        expected = -1.2821;
        assertEquals(expected, dist.inverseCumulativeProbability(0.1587), 0.001);

        expected = 1.2821;
        assertEquals(expected, dist.inverseCumulativeProbability(0.8413), 0.001);

        expected = Double.NEGATIVE_INFINITY;
        assertEquals(expected, dist.inverseCumulativeProbability(0.0), 0.0);

        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, dist.inverseCumulativeProbability(1.0), 0.0);

        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(-0.1));
    }

    @Test
    void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expectedMin = -3.0;
        double expectedMax = 3.0;
        double sample;
        for (int i = 0; i < 100; i++) {
            sample = dist.sample();
            assertTrue(sample >= expectedMin && sample <= expectedMax);
        }
    }

    @Test
    void testNormalDistributionImplWithZeroMeanAndOneStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        double e = 1.0 / (2 * FastMath.SQRT2PI);
        double x = 0.0;
        double p, pExpected;
        for (int i = 0; i < 5; i++) {
            p = dist.cumulativeProbability(x);
            pExpected = 0.5 + e * Erf.erf(x / FastMath.SQRT2);
            assertEquals(pExpected, p, 0.001);
            x += 0.1;
        }
    }

}