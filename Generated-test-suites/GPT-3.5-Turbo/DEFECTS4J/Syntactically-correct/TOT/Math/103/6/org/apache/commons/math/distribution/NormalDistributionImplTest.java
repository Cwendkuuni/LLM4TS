import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normal;
    
    @Before
    public void setUp() {
        normal = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, normal.getMean(), 0);
        normal.setMean(5);
        assertEquals(5, normal.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normal.getStandardDeviation(), 0);
        normal.setStandardDeviation(2.5);
        assertEquals(2.5, normal.getStandardDeviation(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        normal.setStandardDeviation(-1);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normal.cumulativeProbability(0), 0.001);
        assertEquals(0.8413, normal.cumulativeProbability(1), 0.001);
        assertEquals(0.1587, normal.cumulativeProbability(-1), 0.001);
        assertEquals(0.9999, normal.cumulativeProbability(4), 0.001);
        assertEquals(0.0001, normal.cumulativeProbability(-4), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1), 0);
        assertEquals(-1.0, normal.inverseCumulativeProbability(0.1587), 0.001);
        assertEquals(1.0, normal.inverseCumulativeProbability(0.8413), 0.001);
        assertEquals(-4.0, normal.inverseCumulativeProbability(0.0001), 0.001);
        assertEquals(4.0, normal.inverseCumulativeProbability(0.9999), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        normal.inverseCumulativeProbability(2);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.2), 0);
        assertEquals(0, normal.getDomainLowerBound(0.5), 0);
        assertEquals(0, normal.getDomainLowerBound(0.8), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normal.getDomainUpperBound(0.2), 0);
        assertEquals(0, normal.getDomainUpperBound(0.5), 0);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.8), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normal.getInitialDomain(0.2), 0.001);
        assertEquals(0, normal.getInitialDomain(0.5), 0);
        assertEquals(1.0, normal.getInitialDomain(0.8), 0.001);
    }
}