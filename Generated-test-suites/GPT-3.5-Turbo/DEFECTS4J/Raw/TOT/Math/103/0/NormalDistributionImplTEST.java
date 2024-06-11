import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.00001);
    }

    @Test
    public void testSetMean() {
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.00001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.00001);
    }

    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double result = distribution.cumulativeProbability(0.0);
            assertEquals(0.5, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = distribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityZero() {
        try {
            double result = distribution.inverseCumulativeProbability(0.0);
            assertEquals(Double.NEGATIVE_INFINITY, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityOne() {
        try {
            double result = distribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityException() {
        try {
            double result = distribution.inverseCumulativeProbability(1.5);
            fail("Exception not thrown");
        } catch (MathException e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = distribution.getDomainLowerBound(0.25);
        assertEquals(-Double.MAX_VALUE, result, 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = distribution.getDomainUpperBound(0.25);
        assertEquals(Double.MAX_VALUE, result, 0.00001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = distribution.getInitialDomain(0.25);
        assertEquals(-1.0, result, 0.00001);
    }
}