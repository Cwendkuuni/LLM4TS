package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.12566370614359172, dist.probability(0), 0.000001);
        assertEquals(0.1098611696295673, dist.probability(1), 0.000001);
        assertEquals(0.2401089435754242, dist.probability(2), 0.000001);
        assertEquals(0.18045751625629934, dist.probability(3), 0.000001);
        assertEquals(0.13598893371612746, dist.probability(4), 0.000001);
        assertEquals(0.1032106934986782, dist.probability(5), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
        assertEquals(0.12566370614359172, dist.cumulativeProbability(0), 0.000001);
        assertEquals(0.25132741228718345, dist.cumulativeProbability(1), 0.000001);
        assertEquals(0.37700011832987458, dist.cumulativeProbability(2), 0.000001);
        assertEquals(0.49867322451266574, dist.cumulativeProbability(3), 0.000001);
        assertEquals(0.612263630595557, dist.cumulativeProbability(4), 0.000001);
        assertEquals(0.7203519923369183, dist.cumulativeProbability(5), 0.000001);
        assertEquals(0.82318192346028, dist.cumulativeProbability(6), 0.000001);
        assertEquals(0.922468839773712, dist.cumulativeProbability(7), 0.000001);
        assertEquals(0.9999999999999999, dist.cumulativeProbability(10), 0.000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidArgument() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        dist.cumulativeProbability(Integer.MIN_VALUE);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.12566370614359172, dist.probability(0), 0.000001);
        assertEquals(0.1263375428063344, dist.normalApproximateProbability(0), 0.000001);
        assertEquals(0.1098611696295673, dist.probability(1), 0.000001);
        assertEquals(0.1096435758677482, dist.normalApproximateProbability(1), 0.000001);
        assertEquals(0.2401089435754242, dist.probability(2), 0.000001);
        assertEquals(0.23993233850754714, dist.normalApproximateProbability(2), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertTrue(Precision.isClose(dist.sample(), 2, 0.01));
        assertTrue(Precision.isClose(dist.sample(), 3, 0.01));
        assertTrue(Precision.isClose(dist.sample(), 4, 0.01));
        assertTrue(Precision.isClose(dist.sample(), 5, 0.01));
        assertTrue(Precision.isClose(dist.sample(), 6, 0.01));
    }

    @Test
    public void testSampleWithLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10000.0);
        int sample = dist.sample();
        assertTrue(Precision.isClose(sample, 10000.0, 1000.0));
    }

    @Test
    public void testSampleWithSmallMean() throws MathException {
        RandomDataGenerator rnd = new RandomDataGenerator();
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int sample = dist.sample();
        assertTrue(Precision.isClose(sample, 0.5, 0.25));
        // Check that distribution is symmetric around mean
        int sample2 = dist.sample();
        assertTrue(Precision.isClose(sample2, 0.5, 0.25));
        assertEquals(sample, sample2);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0, dist.inverseCumulativeProbability(0));
        assertEquals(1, dist.inverseCumulativeProbability(1));
        assertEquals(2, dist.inverseCumulativeProbability(0.37700011832987458));
        assertEquals(3, dist.inverseCumulativeProbability(0.49867322451266574));
        assertEquals(4, dist.inverseCumulativeProbability(0.612263630595557));
        assertEquals(5, dist.inverseCumulativeProbability(0.7203519923369183));
        assertEquals(6, dist.inverseCumulativeProbability(0.82318192346028));
        assertEquals(7, dist.inverseCumulativeProbability(0.922468839773712));
        assertEquals(10, dist.inverseCumulativeProbability(0.9999999999999999));

        try {
            dist.inverseCumulativeProbability(-1);
        } catch (MathException e) {
            assertTrue(e.getMessage().contains("not in the domain"));
        }

        try {
            dist.inverseCumulativeProbability(1.1);
        } catch (MathException e) {
            assertTrue(e.getMessage().contains("not in the domain"));
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100000.0);
        assertEquals(Integer.MAX_VALUE, dist.inverseCumulativeProbability(1.0));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCumulativeProbabilityWithLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0E307);
        dist.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }
}