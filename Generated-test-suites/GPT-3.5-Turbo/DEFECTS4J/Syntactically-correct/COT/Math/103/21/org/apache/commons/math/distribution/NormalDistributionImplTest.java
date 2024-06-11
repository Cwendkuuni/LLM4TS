// Test cases for NormalDistributionImpl class


import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(10.0);
        assertEquals(10.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double result = normalDistribution.cumulativeProbability(1.0);
            assertEquals(0.841344746, result, 0.000000001);
        } catch (MathException e) {
            fail("MathException thrown.");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            fail("MathException thrown.");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.0);
            assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("MathException thrown.");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("MathException thrown.");
        }
    }

    @Test
    public void testInvalidInverseCumulativeProbability() {
        try {
            normalDistribution.inverseCumulativeProbability(-0.5);
            fail("MathException expected.");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
}