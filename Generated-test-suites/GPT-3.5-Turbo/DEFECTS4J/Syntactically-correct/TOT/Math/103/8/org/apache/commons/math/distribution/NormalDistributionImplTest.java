import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
  
    private NormalDistributionImpl distribution;
  
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
    }
  
    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
  
    @Test
    public void testSetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
  
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.0001);
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviation_Negative() {
        distribution.setStandardDeviation(-2.5);
    }
  
    @Test
    public void testSetStandardDeviation_Positive() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.0001);
    }
  
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.84134, distribution.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.15865, distribution.cumulativeProbability(-1.0), 0.0001);
    }
  
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(-1.6448536269514729, distribution.inverseCumulativeProbability(0.05), 0.0001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(1.6448536269514729, distribution.inverseCumulativeProbability(0.95), 0.0001);
    }
  
    @Test
    public void testInverseCumulativeProbability_Zero() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
    }
  
    @Test
    public void testInverseCumulativeProbability_One() throws MathException {
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbability_Illegal() throws MathException {
        distribution.inverseCumulativeProbability(2.0);
    }
  
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
    }
  
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, distribution.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getDomainUpperBound(0.5), 0.0001);
    }
  
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.0001);
        assertEquals(1.0, distribution.getInitialDomain(1.0), 0.0001);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0001);
    }

}