package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchValueException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double, double)}.
     */
    @Test
    public void testNormalDistributionImplConstructorWithAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1, 1e-6);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertEquals(1e-6, dist.getSolverAbsoluteAccuracy(), 1e-7);
    }

    /**
     * Test method for {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0.0);
        assertEquals(0.398942280401, density, 1e-8);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = dist.cumulativeProbability(0.5);
        assertEquals(0.682689492137, p, 1e-8);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() 
            throws MathException, NoSuchValueException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 1e-8);

        p = 0.1;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-1.28155169232, x, 1e-8);

        p = 0.999;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(3.08579250621, x, 1e-8);

        p = 1.1;
        assertFalse(dist.hasInverseCumulativeProbability(p));
    }

    /**
     * Test method for {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testNormalDistributionImplSample() throws MathException {
        RandomDataGenerator rng = new RandomDataGenerator();
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x;
        for (int i = 0; i < 1000; i++) {
            x = dist.sample(rng);
            assertTrue(x >= -3.0 && x <= 3.0);
        }
    }
}