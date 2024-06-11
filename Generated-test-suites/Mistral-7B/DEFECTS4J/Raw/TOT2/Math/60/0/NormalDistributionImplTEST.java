package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {

    private NormalDistribution distribution;

    @Before
    public void setup() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
    }

    // Test method: getMean()

    @Test
    public void testGetMean() {
        assertEquals("Mean is incorrect", 0.0, distribution.getMean(), 1e-12);
    }

    // Test method: getStandardDeviation()

    @Test
    public void testGetStandardDeviation() {
        assertEquals("Standard deviation is incorrect", 1.0, distribution.getStandardDeviation(), 1e-12);
    }

    // Test method: density(double x)

    @Test
    public void testDensity() {
        double density = distribution.density(0.0);
        assertEquals("Density is incorrect for x = 0", 0.39894228040143273, density, 1e-8);

        density = distribution.density(2.0);
        assertEquals("Density is incorrect for x = 2", 0.00688115217861419, density, 1e-8);
    }

    // Test method: cumulativeProbability(double x)

    @Test
    public void testCumulativeProbability() throws MathRuntimeException {
        assertEquals("CDF is incorrect for x = 0", 0.5, distribution.cumulativeProbability(0.0), 1e-12);
        assertEquals("CDF is incorrect for x = 1", 0.84134474606898369, distribution.cumulativeProbability(1.0), 1e-12);
        assertEquals("CDF is incorrect for x = 2", 0.97724547466151523, distribution.cumulativeProbability(2.0), 1e-12);
    }

    // Test method: inverseCumulativeProbability(double p)

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidProbability() {
        distribution.inverseCumulativeProbability(-0.5);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathRuntimeException {
        assertEquals("Inverse CDF is incorrect for p = 0.1", -0.8413447460689837, distribution.inverseCumulativeProbability(0.1), 1e-8);
        assertEquals("Inverse CDF is incorrect for p = 0.5", 0.0, distribution.inverseCumulativeProbability(0.5), 1e-12);
        assertEquals("Inverse CDF is incorrect for p = 0.9", 0.8413447460689833, distribution.inverseCumulativeProbability(0.9), 1e-8);
    }

    // Test method: sample()

    @Test
    public void testSample() throws MathRuntimeException {
        double sample = distribution.sample();
        assertEquals("Sample is incorrect", 0.0, sample, 1e-6);

        sample = distribution.sample();
        assertTrue("Sample is not within the expected range", sample >= -2.5 && sample <= 2.5);
    }
}