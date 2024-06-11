package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test creation with valid parameters.
     */
    @Test
    public void testCreation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.0);
        assertEquals(1, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test creation with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testCreationNegativeStdDev() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test density method with valid input.
     */
    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0.5);
        assertEquals(0.12012693639272136, density, 1e-6);
    }

    /**
     * Test density method with extreme input.
     */
    @Test
    public void testDensityExtreme() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(-100);
        assertEquals(0, density, 1e-20);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double cp = dist.cumulativeProbability(0.5);
        assertEquals(0.682689492137, cp, 1e-6);
    }

    /**
     * Test cumulativeProbability method with extreme input.
     */
    @Test
    public void testCumulativeProbabilityExtreme() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double cp = dist.cumulativeProbability(-100);
        assertEquals(0, cp, 1e-20);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0, Precision.compareTo(x, 0.5, 1e-6));
    }

    /**
     * Test inverseCumulativeProbability method with extreme input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityExtreme() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double x = dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSample() throws MathException {
        RandomGenerator rg = new org.apache.commons.math3.random.MersenneTwisterFastRandom();
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample(rg);
            // System.out.println(sample);
            assertTrue(sample >= -3 && sample <= 3);
        }
    }

    /**
     * Test sample method with extreme input.
     */
    @Test
    public void testSampleExtreme() throws MathException {
        RandomGenerator rg = new org.apache.commons.math3.random.MersenneTwisterFastRandom();
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample(rg);
            // System.out.println(sample);
            if (sample < -10 || sample > 10) {
                fail("Sample is out of bounds: " + sample);
            }
        }
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-9);
    }

    /**
     * Test Erf method in cumulativeProbability method.
     */
    @Test
    public void testErf() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double erf = Erf.erf(1.5);
        double cp = dist.cumulativeProbability(1.5);
        double expected = 0.999371038758;
        assertEquals(expected, cp, 1e-6);
        assertEquals(expected, 1.0 - erf, 1e-6);
    }

}