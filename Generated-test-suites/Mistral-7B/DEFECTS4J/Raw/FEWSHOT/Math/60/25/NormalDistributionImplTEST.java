package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.special.Erf;

import java.util.Random;

public class NormalDistributionImplTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStdDev() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(1, dist.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(2, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(0.117202075, dist.density(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(0.157079632679, dist.cumulativeProbability(2), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        dist.cumulativeProbability(100);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(0.99956429, dist.inverseCumulativeProbability(0.999), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNotSupported() throws MathException {
        new NormalDistributionImpl(1, 2).inverseCumulativeProbability(0.5);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        Random random = new Random();
        assertEquals(dist.sample(), dist.sample(), 0.001);
        assertEquals(dist.sample(), random.nextGaussian(1, 2), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityConvergenceFailure() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        dist.inverseCumulativeProbability(0.1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        dist.inverseCumulativeProbability(1.1);
    }
}