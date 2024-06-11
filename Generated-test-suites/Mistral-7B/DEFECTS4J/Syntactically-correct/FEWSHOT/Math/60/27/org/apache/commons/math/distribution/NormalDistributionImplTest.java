package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.distribution.NormalDistribution;
import org.apache.commons.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.commons.math.MathException;
import org.apache.commons.commons.math.special.Erf;

import junit.framework.TestCase;

public class NormalDistributionImplTest extends TestCase {

    /**
     * Test constructor with valid parameters.
     */
    public void testConstructorValid() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with negative standard deviation.
     */
    public void testConstructorInvalidNegativeStdDev() {
        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    /**
     * Test density method.
     */
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 0.5;
        double density = dist.density(x);
        assertEquals(0.15430801585505611, density, 0.0000001);
    }

    /**
     * Test cumulativeProbability method.
     */
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 0.5;
        double p = dist.cumulativeProbability(x);
        assertEquals(0.5, p, 0.000001);
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.5, x, 0.000001);
    }

    /**
     * Test sample method.
     */
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = dist.sample();
        assertTrue(Double.isFinite(x));
        assertEquals(0.0, dist.cumulativeProbability(x), 0.000001);
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test getMean method.
     */
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 0.0);
        assertEquals(1.0, dist.getMean(), 0.0);
    }

    /**
     * Test getStandardDeviation method.
     */
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test Erf method.
     */
    public void testErf() {
        double x = 1.0;
        double expected = 0.842700792858443;
        assertEquals(expected, Erf.erf(x / (2 * FastMath.sqrt(2.0))), 0.00001);
    }

}