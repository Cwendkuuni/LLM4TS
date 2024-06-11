package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.01);
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
    }

    @Test
    public void testNormalDistributionImplConstructionWithMeanAndStdDev() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.01);
        assertEquals(2.0, dist.getStandardDeviation(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructionWithNegativeStdDev() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double density;

        density = dist.density(0.5);
        assertEquals(0.1914521633622031, density, 0.00000001);

        density = dist.density(-3.0);
        assertEquals(0.002875737385461265, density, 0.00000001);

        density = dist.density(3.0);
        assertEquals(0.002875737385461265, density, 0.00000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.1547583955277432, dist.cumulativeProbability(0.55), 0.0001);
        assertEquals(0.8452416044722568, dist.cumulativeProbability(1.5), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(-5.0), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5.0), 0.0001);

        assertThrows(MaxIterationsExceededException.class, () -> dist.cumulativeProbability(-50.0));
        assertThrows(MaxIterationsExceededException.class, () -> dist.cumulativeProbability(50.0));
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.01);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.01);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.01);
        assertNull(dist.inverseCumulativeProbability(-1.0));
        assertNull(dist.inverseCumulativeProbability(2.0));

        // Test with large values
        double p = 0.99999999;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(5.99999999, x, 0.0001);

        p = 0.0000001;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-5.99999999, x, 0.0001);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 1000; i++) {
            double x = dist.sample();
            assertTrue(Precision.isClose(0.0, dist.cumulativeProbability(x), 0.01));
        }
    }

    @Test
    public void testNormalDistributionImplDomainBounds() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        double lower, upper, initial;

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            lower = dist.getDomainLowerBound(p);
            upper = dist.getDomainUpperBound(p);
            initial = dist.getInitialDomain(p);

            if (p < 0.5) {
                assertEquals(Double.NEGATIVE_INFINITY, lower, 0.0);
                assertEquals(mean, upper, 0.01);
                assertEquals(mean - standardDeviation, initial, 0.01);
            } else {
                assertEquals(mean, lower, 0.0);
                assertEquals(Double.POSITIVE_INFINITY, upper, 0.0);
                assertEquals(mean + standardDeviation, initial, 0.01);
            }
        }
    }

    @Test
    public void testNormalDistributionImplInverseCumProbWithZeroMean() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.01);

        p = 0.6821;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.6795, x, 0.01);

        p = 1.2821;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.2795, x, 0.01);
    }

    @Test
    public void testNormalDistributionImplInverseCumProbWithMeanAndStdDev() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);

        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.5, x, 0.01);

        p = 0.45;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.4, x, 0.01);

        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(3.0, x, 0.01);

        p = 0.95;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(4.5, x, 0.01);
    }
}