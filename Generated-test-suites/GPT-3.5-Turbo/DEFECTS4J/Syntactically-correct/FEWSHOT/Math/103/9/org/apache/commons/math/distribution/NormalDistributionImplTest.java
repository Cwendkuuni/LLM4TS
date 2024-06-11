import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.000001);
    }

    @Test
    public void testSetMean() {
        double newMean = 2.5;
        distribution.setMean(newMean);
        assertEquals(newMean, distribution.getMean(), 0.000001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        distribution.setStandardDeviation(-1.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        double newStandardDeviation = 2.0;
        distribution.setStandardDeviation(newStandardDeviation);
        assertEquals(newStandardDeviation, distribution.getStandardDeviation(), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 0.0;
        double expected = 0.5;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expected = 0.0;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBoundLessThanHalf() {
        double p = 0.25;
        double expected = -Double.MAX_VALUE;
        double result = distribution.getDomainLowerBound(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBoundGreaterThanHalf() {
        double p = 0.75;
        double expected = 0.0;
        double result = distribution.getDomainLowerBound(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBoundLessThanHalf() {
        double p = 0.25;
        double expected = 0.0;
        double result = distribution.getDomainUpperBound(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBoundGreaterThanHalf() {
        double p = 0.75;
        double expected = Double.MAX_VALUE;
        double result = distribution.getDomainUpperBound(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetInitialDomainLessThanHalf() {
        double p = 0.25;
        double expected = -1.0;
        double result = distribution.getInitialDomain(p);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testGetInitialDomainGreaterThanHalf() {
        double p = 0.75;
        double expected = 1.0;
        double result = distribution.getInitialDomain(p);
        assertEquals(expected, result, 0.000001);
    }
}