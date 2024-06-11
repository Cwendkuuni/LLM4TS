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
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testDensity() {
        // Typical case
        double x1 = 0;
        double expectedDensity1 = 0.398942;
        double actualDensity1 = normalDistribution.density(x1);
        assertEquals(expectedDensity1, actualDensity1, 1e-6);

        // Edge case
        double x2 = Double.POSITIVE_INFINITY;
        double expectedDensity2 = 0;
        double actualDensity2 = normalDistribution.density(x2);
        assertEquals(expectedDensity2, actualDensity2, 1e-6);

        // Error scenario
        try {
            double x3 = Double.NaN;
            normalDistribution.density(x3);
            fail("MathException should have been thrown, as input is not a number");
        } catch (MathException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCumulativeProbability() {
        // Typical case
        double x1 = 0;
        double expectedCumulativeProbability1 = 0.5;
        try {
            double actualCumulativeProbability1 = normalDistribution.cumulativeProbability(x1);
            assertEquals(expectedCumulativeProbability1, actualCumulativeProbability1, 1e-6);
        } catch (MathException e) {
            fail("No exception should be thrown");
        }

        // Edge case
        double x2 = Double.NEGATIVE_INFINITY;
        double expectedCumulativeProbability2 = 0;
        try {
            double actualCumulativeProbability2 = normalDistribution.cumulativeProbability(x2);
            assertEquals(expectedCumulativeProbability2, actualCumulativeProbability2, 1e-6);
        } catch (MathException e) {
            fail("No exception should be thrown");
        }

        // Error scenario
        try {
            double x3 = Double.NaN;
            normalDistribution.cumulativeProbability(x3);
            fail("MathException should have been thrown, as input is not a number");
        } catch (MathException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Typical case
        double p1 = 0.5;
        double expectedInverseCumulativeProbability1 = 0;
        try {
            double actualInverseCumulativeProbability1 = normalDistribution.inverseCumulativeProbability(p1);
            assertEquals(expectedInverseCumulativeProbability1, actualInverseCumulativeProbability1, 1e-6);
        } catch (MathException e) {
            fail("No exception should be thrown");
        }

        // Edge case
        double p2 = 0;
        double expectedInverseCumulativeProbability2 = Double.NEGATIVE_INFINITY;
        try {
            double actualInverseCumulativeProbability2 = normalDistribution.inverseCumulativeProbability(p2);
            assertEquals(expectedInverseCumulativeProbability2, actualInverseCumulativeProbability2, 1e-6);
        } catch (MathException e) {
            fail("No exception should be thrown");
        }

        double p3 = 1;
        double expectedInverseCumulativeProbability3 = Double.POSITIVE_INFINITY;
        try {
            double actualInverseCumulativeProbability3 = normalDistribution.inverseCumulativeProbability(p3);
            assertEquals(expectedInverseCumulativeProbability3, actualInverseCumulativeProbability3, 1e-6);
        } catch (MathException e) {
            fail("No exception should be thrown");
        }

        // Error scenario
        try {
            double p4 = -1;
            normalDistribution.inverseCumulativeProbability(p4);
            fail("MathException should have been thrown, as input is not a valid probability");
        } catch (MathException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSample() {
        // No edge cases or error scenarios

        // Typical case
        try {
            double sample = normalDistribution.sample();
            assertTrue(Double.isFinite(sample));
        } catch (MathException e) {
            fail("No exception should be thrown");
        }
    }

}