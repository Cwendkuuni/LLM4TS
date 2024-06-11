import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.100811, fDist.density(2.0), 0.000001);
        assertEquals(0.0403, fDist.density(1.0), 0.000001);
        assertEquals(0.797884, fDist.density(0.5), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.717963, fDist.cumulativeProbability(0.5), 0.000001);
        assertEquals(0.889092, fDist.cumulativeProbability(1.0), 0.000001);
        assertEquals(0.973000, fDist.cumulativeProbability(2.0), 0.000001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(5, fDist.getNumeratorDegreesOfFreedom(), 0.000001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(10, fDist.getDenominatorDegreesOfFreedom(), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(1.25, fDist.getNumericalMean(), 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(3.75, fDist.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0, fDist.getSupportLowerBound(), 0.000001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.000001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 10);
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 10);
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(5, 10);
        assertTrue(fDist.isSupportConnected());
    }
    
}