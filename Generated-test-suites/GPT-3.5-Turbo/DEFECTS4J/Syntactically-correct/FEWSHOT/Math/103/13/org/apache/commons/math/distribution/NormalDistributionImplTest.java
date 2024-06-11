import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        distribution.setMean(1);
        assertEquals(1, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = distribution.cumulativeProbability(0);
        assertEquals(0.5, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityMinValue() throws MathException {
        double result = distribution.inverseCumulativeProbability(Double.MIN_VALUE);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityMaxValue() throws MathException {
        double result = distribution.inverseCumulativeProbability(Double.MAX_VALUE);
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationZero() {
        distribution.setStandardDeviation(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        distribution.setStandardDeviation(-1);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = distribution.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = distribution.getInitialDomain(0.5);
        assertEquals(1, result, 0);
    }
    
}