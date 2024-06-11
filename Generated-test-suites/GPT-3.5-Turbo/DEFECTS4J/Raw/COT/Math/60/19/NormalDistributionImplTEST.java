
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(2, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }
  
    // Additional test cases for other methods

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(0.05399096651318806, distribution.density(1), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        try {
            assertEquals(0.15865525393145707, distribution.cumulativeProbability(1), 1e-15);
        } catch (MathException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        try {
            assertEquals(-0.6744897501960817, distribution.inverseCumulativeProbability(0.25), 1e-15);
        } catch (MathException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        try {
            distribution.sample(); // Just test if no exception is thrown
        } catch (MathException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double lowerBound = distribution.getDomainLowerBound(0.2);
        assertTrue(lowerBound < -Double.MAX_VALUE);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double upperBound = distribution.getDomainUpperBound(0.8);
        assertTrue(upperBound > Double.MAX_VALUE);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double initialDomain = distribution.getInitialDomain(0.5);
        assertEquals(1, initialDomain, 1e-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(2, -1);
    }
}