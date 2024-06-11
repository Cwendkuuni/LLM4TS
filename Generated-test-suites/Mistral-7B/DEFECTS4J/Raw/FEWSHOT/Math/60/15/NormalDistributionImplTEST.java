package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistribution dist;
    private RandomDataGenerator rdg;

    @Before
    public void setUp() {
        dist = new NormalDistributionImpl(0, 1);
        rdg = dist.getRandomGenerator();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, dist.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testGetStandardDeviationNegative() {
        NormalDistributionImpl distNeg = new NormalDistributionImpl(0, -1);
        assertEquals(-1, distNeg.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorIllegalStandardDeviation() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testDensity() {
        double x = 0;
        double density = dist.density(x);
        assertTrue(density > 0);
        x = 1.5;
        density = dist.density(x);
        assertTrue(density > 0);
        x = 5;
        density = dist.density(x);
        assertTrue(density > 0);
        x = 10;
        density = dist.density(x);
        assertTrue(density < 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x;

        x = -3;
        assertEquals(0, dist.cumulativeProbability(x), 0.001);
        x = -2;
        assertEquals(0.1543081139034, dist.cumulativeProbability(x), 0.001);
        x = -1;
        assertEquals(0.3413447460834, dist.cumulativeProbability(x), 0.001);
        x = 0;
        assertEquals(0.5, dist.cumulativeProbability(x), 0.001);
        x = 1;
        assertEquals(0.8413447460834, dist.cumulativeProbability(x), 0.001);
        x = 2;
        assertEquals(0.9752745854928, dist.cumulativeProbability(x), 0.001);
        x = 3;
        assertEquals(1, dist.cumulativeProbability(x), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        dist.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p;

        p = 0;
        assertEquals(-1.280667130496, dist.inverseCumulativeProbability(p), 0.001);
        p = 0.1;
        assertEquals(-0.55481271865, dist.inverseCumulativeProbability(p), 0.001);
        p = 0.5;
        assertEquals(0, dist.inverseCumulativeProbability(p), 0.001);
        p = 0.9;
        assertEquals(0.55481271865, dist.inverseCumulativeProbability(p), 0.001);
        p = 1;
        assertEquals(1.280667130496, dist.inverseCumulativeProbability(p), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        dist.inverseCumulativeProbability(2);
    }

    @Test
    public void testSample() throws MathException {
        double sample;
        for (int i = 0; i < 10; i++) {
            sample = dist.sample();
            assertEquals(0, dist.cumulativeProbability(sample), 0.001);
        }
    }

    @Test
    public void testSampleNegativeStandardDeviation() throws MathException {
        NormalDistributionImpl distNeg = new NormalDistributionImpl(0, -1);
        double sampleNeg = distNeg.sample();
        assertEquals(1, distNeg.cumulativeProbability(sampleNeg), 0.001);
    }

    @Test
    public void testSampleManyTimes() throws MathException {
        double[] samples = new double[10000];
        for (int i = 0; i < 10000; i++) {
            samples[i] = dist.sample();
        }
        for (int i = 0; i < 10000; i++) {
            assertEquals(0.5, dist.cumulativeProbability(samples[i]), 0.01);
        }
    }
}