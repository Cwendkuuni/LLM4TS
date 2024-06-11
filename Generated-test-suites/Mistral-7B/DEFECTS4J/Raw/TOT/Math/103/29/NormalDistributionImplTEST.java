package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(1.2, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with default mean and standard deviation.
     */
    @Test
    public void testConstructorWithDefaultMeanAndStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test setting mean and standard deviation.
     */
    @Test
    public void testSettingMeanAndStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(3.14);
        dist.setStandardDeviation(2.71);
        assertEquals(3.14, dist.getMean(), 0.001);
        assertEquals(2.71, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test mean accessor.
     */
    @Test
    public void testMeanAccessor() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
    }

    /**
     * Test mean setter.
     */
    @Test
    public void testMeanSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(1.23);
        assertEquals(1.23, dist.getMean(), 0.001);
    }

    /**
     * Test standard deviation accessor.
     */
    @Test
    public void testStandardDeviationAccessor() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test standard deviation setter.
     */
    @Test
    public void testStandardDeviationSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0.56);
        assertEquals(0.56, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test cumulative probability calculation.
     */
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Test valid input
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841345, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.158655, dist.cumulativeProbability(0.0), 0.001);

        // Test extreme values
        try {
            dist.cumulativeProbability(-5.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(50.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test edge cases
        NormalDistributionImpl distEdge = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distEdge.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, distEdge.cumulativeProbability(1.0), 0.001);
    }

    /**
     * Test inverse cumulative probability calculation.
     */
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Test valid input
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.28155, dist.inverseCumulativeProbability(0.841345), 0.001);

        // Test extreme values
        try {
            dist.inverseCumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test edge cases
        NormalDistributionImpl distEdge = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distEdge.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.0, distEdge.inverseCumulativeProbability(0.5), 0.001);
    }
}