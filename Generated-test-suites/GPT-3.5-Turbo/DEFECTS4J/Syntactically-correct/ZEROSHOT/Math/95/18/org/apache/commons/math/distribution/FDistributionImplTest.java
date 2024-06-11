import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
  
    @Test
    public void testCumulativeProbability() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(0.200, f.cumulativeProbability(0.25), 0.001);
        assertEquals(0.500, f.cumulativeProbability(0.5), 0.001);
        assertEquals(0.800, f.cumulativeProbability(0.75), 0.001);
    }
  
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(0.250, f.inverseCumulativeProbability(0.2), 0.001);
        assertEquals(0.500, f.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(0.750, f.inverseCumulativeProbability(0.8), 0.001);
    }
  
    @Test
    public void testGetDomainLowerBound() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(0.0, f.getDomainLowerBound(0.25), 0.001);
    }
  
    @Test
    public void testGetDomainUpperBound() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0.75), 0.001);
    }
  
    @Test
    public void testGetInitialDomain() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(1.2, f.getInitialDomain(0.5), 0.001);
    }
  
    @Test
    public void testSetNumeratorDegreesOfFreedom() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        f.setNumeratorDegreesOfFreedom(4);
        assertEquals(4.0, f.getNumeratorDegreesOfFreedom(), 0.001);
    }
  
    @Test(expected=IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        f.setNumeratorDegreesOfFreedom(0);
    }
  
    @Test
    public void testGetNumeratorDegreesOfFreedom() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(3.0, f.getNumeratorDegreesOfFreedom(), 0.001);
    }
  
    @Test
    public void testSetDenominatorDegreesOfFreedom() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        f.setDenominatorDegreesOfFreedom(4);
        assertEquals(4.0, f.getDenominatorDegreesOfFreedom(), 0.001);
    }
  
    @Test(expected=IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        f.setDenominatorDegreesOfFreedom(0);
    }
  
    @Test
    public void testGetDenominatorDegreesOfFreedom() throws Exception {
        FDistributionImpl f = new FDistributionImpl(3, 3);
        assertEquals(3.0, f.getDenominatorDegreesOfFreedom(), 0.001);
    }
  
}