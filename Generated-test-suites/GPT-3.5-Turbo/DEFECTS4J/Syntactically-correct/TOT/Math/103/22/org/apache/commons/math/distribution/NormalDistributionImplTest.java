import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setup() {
        normalDistribution = new NormalDistributionImpl();
    }

    // Test for getMean() method
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }

    // Test for setMean() method
    @Test
    public void testSetMean() {
        double mean = 2.5;
        normalDistribution.setMean(mean);
        assertEquals(mean, normalDistribution.getMean(), 0.0);
    }

    // Test for getStandardDeviation() method
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    // Test for setStandardDeviation() method
    @Test
    public void testSetStandardDeviation() {
        double sd = 3.0;
        normalDistribution.setStandardDeviation(sd);
        assertEquals(sd, normalDistribution.getStandardDeviation(), 0.0);
    }

    // Test for cumulativeProbability() method - typical use case
    @Test
    public void testCumulativeProbability_typical() {
        double x = 1.5;
        double expected = 0.933192;
        try {
            double result = normalDistribution.cumulativeProbability(x);
            assertEquals(expected, result, 0.000001);
        } catch (MathException e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    // Test for cumulativeProbability() method - edge case
    @Test
    public void testCumulativeProbability_edge() {
        double x = Double.POSITIVE_INFINITY;
        double expected = 1.0;
        try {
            double result = normalDistribution.cumulativeProbability(x);
            assertEquals(expected, result, 0.0);
        } catch (MathException e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    // Test for cumulativeProbability() method - error scenario
    @Test
    public void testCumulativeProbability_error() {
        double x = Double.NaN;
        try {
            normalDistribution.cumulativeProbability(x);
            fail("An exception should have been thrown.");
        } catch (MathException e) {
            assertTrue(true); // Exception was caught successfully
        }
    }

    // Test for inverseCumulativeProbability() method - typical use case
    @Test
    public void testInverseCumulativeProbability_typical() {
        double p = 0.7;
        double expected = 0.524401;
        try {
            double result = normalDistribution.inverseCumulativeProbability(p);
            assertEquals(expected, result, 0.000001);
        } catch (MathException e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    // Test for inverseCumulativeProbability() method - edge case
    @Test
    public void testInverseCumulativeProbability_edge() {
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        try {
            double result = normalDistribution.inverseCumulativeProbability(p);
            assertEquals(expected, result, 0.0);
        } catch (MathException e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    // Test for inverseCumulativeProbability() method - error scenario
    @Test
    public void testInverseCumulativeProbability_error() {
        double p = -0.5;
        try {
            normalDistribution.inverseCumulativeProbability(p);
            fail("An exception should have been thrown.");
        } catch (MathException e) {
            assertTrue(true); // Exception was caught successfully
        }
    }

    // Test for getDomainLowerBound() method
    @Test
    public void testGetDomainLowerBound() {
        double p = 0.3;
        double expected = -Double.MAX_VALUE;
        double result = normalDistribution.getDomainLowerBound(p);
        assertEquals(expected, result, 0.0);
    }

    // Test for getDomainUpperBound() method
    @Test
    public void testGetDomainUpperBound() {
        double p = 0.7;
        double expected = Double.MAX_VALUE;
        double result = normalDistribution.getDomainUpperBound(p);
        assertEquals(expected, result, 0.0);
    }

    // Test for getInitialDomain() method
    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expected = 0.0;
        double result = normalDistribution.getInitialDomain(p);
        assertEquals(expected, result, 0.0);
    }

}