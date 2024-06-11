package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.AbsNormalDistribution;
import org.junit.jupiter.api.Test;

class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        // Test constructor NormalDistributionImpl(double mean, double sd)
        NormalDistributionImpl normalDist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normalDist.getMean(), 0.001);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.001);

        // Edge cases for constructor NormalDistributionImpl(double mean, double sd)
        NormalDistributionImpl normalDistNaNMean = new NormalDistributionImpl(Double.NaN, 2.0);
        assertEquals(Double.NaN, normalDistNaNMean.getMean(), 0.001);
        NormalDistributionImpl normalDistNaNStdDev = new NormalDistributionImpl(1.0, Double.NaN);
        assertEquals(1.0, normalDistNaNStdDev.getMean(), 0.001);
        assertEquals(Double.NaN, normalDistNaNStdDev.getStandardDeviation(), 0.001);

        // Exception handling for constructor NormalDistributionImpl(double mean, double sd)
        try {
            NormalDistributionImpl normalDistNegativeSd = new NormalDistributionImpl(1.0, -1.0);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Standard deviation must be positive.", e.getMessage());
        }

        try {
            NormalDistributionImpl normalDistMeanOutOfRange = new NormalDistributionImpl(1.0, 10.0);
            assertEquals(1.0, normalDistMeanOutOfRange.getMean(), 0.001);
            assertEquals(10.0, normalDistMeanOutOfRange.getStandardDeviation(), 0.001);
        } catch (MathException e) {
            // Expected, meaning mean+3*sd is out of range
        }
    }

    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        // Test constructor NormalDistributionImpl()
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.001);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.001);
    }

    // Other test cases for other methods go here

    @Test
    public void testGetMean() {
        // ... Test code for getMean()
    }

    @Test
    public void testSetMeanThrowsIAE() {
        // ... Test code for setMean(double mean)
    }

    @Test
    public void testGetStandardDeviation() {
        // ... Test code for getStandardDeviation()
    }

    @Test
    public void testSetStandardDeviationThrowsIAE() {
        // ... Test code for setStandardDeviation(double sd)
    }

    @Test
    public void testCumulativeProbability() {
        // ... Test code for cumulativeProbability(double x)
    }

    @Test
    public void testInverseCumulativeProbability() {
        // ... Test code for inverseCumulativeProbability(double p)
    }
}