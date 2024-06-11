import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionTest {
    
    private FDistribution fDistribution;
    
    @Before
    public void setup() {
        fDistribution = new FDistribution(5, 10);
    }
    
    @Test
    public void testDensity_typicalCase() {
        double x = 2.5;
        double expectedDensity = 0.077177;
        double delta = 1e-6;
        
        assertEquals(expectedDensity, fDistribution.density(x), delta);
    }
    
    @Test
    public void testDensity_edgeCase_xEqualsZero() {
        double x = 0;
        double expectedDensity = 0;
        double delta = 1e-6;
        
        assertEquals(expectedDensity, fDistribution.density(x), delta);
    }
    
    @Test
    public void testDensity_edgeCase_xEqualsInfinity() {
        double x = Double.POSITIVE_INFINITY;
        double expectedDensity = 0;
        double delta = 1e-6;
        
        assertEquals(expectedDensity, fDistribution.density(x), delta);
    }
    
    @Test
    public void testCumulativeProbability_typicalCase() {
        double x = 2.5;
        double expectedCumulativeProbability = 0.676801;
        double delta = 1e-6;
        
        assertEquals(expectedCumulativeProbability, fDistribution.cumulativeProbability(x), delta);
    }
    
    @Test
    public void testCumulativeProbability_edgeCase_xEqualsZero() {
        double x = 0;
        double expectedCumulativeProbability = 0;
        double delta = 1e-6;
        
        assertEquals(expectedCumulativeProbability, fDistribution.cumulativeProbability(x), delta);
    }
    
    @Test
    public void testCumulativeProbability_edgeCase_xEqualsInfinity() {
        double x = Double.POSITIVE_INFINITY;
        double expectedCumulativeProbability = 1;
        double delta = 1e-6;
        
        assertEquals(expectedCumulativeProbability, fDistribution.cumulativeProbability(x), delta);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDF = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numeratorDF, 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDF = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denominatorDF, 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(10.0/8.0, numericalMean, 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(560.0/225.0, numericalVariance, 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }
}