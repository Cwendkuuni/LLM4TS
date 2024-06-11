package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.RandomDataGenerator;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        // Test with valid parameters
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Mean should be set correctly.", 0.0, distribution.getMean(), 1e-6);
        assertEquals("Standard deviation should be set correctly.", 1.0, distribution.getStandardDeviation(), 1e-6);

        // Test with negative standard deviation
        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with zero standard deviation
        try {
            new NormalDistributionImpl(0.0, 0.0);
            fail("Expected NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid input
        double x = 1.0;
        double density = distribution.density(x);
        assertEquals("Density should be correct.", 0.3989422804014327, density, 1e-5);

        // Test with negative input
        x = -1.0;
        density = distribution.density(x);
        assertEquals("Density should be correct.", 0.3989422804014327, density, 1e-5);

        // Test with very large input
        x = Double.POSITIVE_INFINITY;
        density = distribution.density(x);
        assertEquals("Density should be zero for large input.", 0.0, density, 1e-5);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid input
        double x = 0.5;
        double probability = distribution.cumulativeProbability(x);
        assertEquals("Probability should be correct.", 0.6826894621363756, probability, 1e-6);

        // Test with very small input
        x = -5.0;
        probability = distribution.cumulativeProbability(x);
        assertEquals("Probability should be zero for very small input.", 0.0, probability, 1e-6);

        // Test with very large input
        x = 5.0;
        probability = distribution.cumulativeProbability(x);
        assertEquals("Probability should be one for very large input.", 1.0, probability, 1e-6);

        // Test with input outside the normal distribution's range
        try {
            distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }

        try {
            distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            double density = distribution.density(sample);
            assertTrue("Samples should be drawn from the distribution.", density > 0.0);
        }
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid input
        double probability = 0.25;
        double x = distribution.inverseCumulativeProbability(probability);
        assertEquals("Inverse cumulative probability should be correct.", 0.0, x, 1e-6);

        // Test with probability very close to 0
        probability = 1.0e-10;
        x = distribution.inverseCumulativeProbability(probability);
        assertEquals("Inverse cumulative probability should be very small.", -3.086, x, 1e-3);

        // Test with probability very close to 1
        probability = 0.99999999999;
        x = distribution.inverseCumulativeProbability(probability);
        assertEquals("Inverse cumulative probability should be very large.", 3.086, x, 1e-3);

        // Test with probability outside the normal distribution's range
        try {
            distribution.inverseCumulativeProbability(-1.0);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }

        try {
            distribution.inverseCumulativeProbability(1.1);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }
    }
}