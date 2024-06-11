import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double density = fDistribution.density(2);
        assertEquals(0.06880798693326062, density, 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double cumulativeProbability = fDistribution.cumulativeProbability(2);
        assertEquals(0.8787093590703583, cumulativeProbability, 0.000001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numeratorDegreesOfFreedom, 0.000001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denominatorDegreesOfFreedom, 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(2.5, numericalMean, 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(12.777777777777779, numericalVariance, 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 0.000001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.000001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(isSupportLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        assertFalse(isSupportUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
    
}