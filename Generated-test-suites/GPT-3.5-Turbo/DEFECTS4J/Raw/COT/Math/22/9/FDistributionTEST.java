
import org.junit.Test;
import org.apache.commons.math3.distribution.*;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(0.0, f.density(-1), 0.0001);
        assertEquals(0.5, f.density(0), 0.0001);
        assertEquals(0.1605, f.density(1), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(0.0, f.cumulativeProbability(-1), 0.0001);
        assertEquals(0.5, f.cumulativeProbability(0), 0.0001);
        assertEquals(0.2718, f.cumulativeProbability(1), 0.0001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(1.0, f.getNumeratorDegreesOfFreedom(), 0.0001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(2.0, f.getDenominatorDegreesOfFreedom(), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(1.6, f.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(24.0, f.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(0.0, f.getSupportLowerBound(), 0.0001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 0.0001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(true, f.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(false, f.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(1, 2);
        assertEquals(true, f.isSupportConnected());
    }

}
