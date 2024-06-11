import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    
    private FDistribution fDistribution;
    private double numeratorDegreesOfFreedom;
    private double denominatorDegreesOfFreedom;
    
    @Before
    public void setUp() {
        numeratorDegreesOfFreedom = 2.0;
        denominatorDegreesOfFreedom = 3.0;
        
        fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test
    public void testDensity() {
        double x = 1.0;
        double expectedDensity = 0.3183098861837907;
        
        double actualDensity = fDistribution.density(x);
        
        assertEquals(expectedDensity, actualDensity, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        double x = 1.0;
        double expectedProbability = 0.5454545454545454;
        
        double actualProbability = fDistribution.cumulativeProbability(x);
        
        assertEquals(expectedProbability, actualProbability, 0.00001);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        numeratorDegreesOfFreedom = -1.0;
        
        fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        denominatorDegreesOfFreedom = -1.0;
        
        fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expectedDegreesOfFreedom = 2.0;
        
        double actualDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        
        assertEquals(expectedDegreesOfFreedom, actualDegreesOfFreedom, 0.00001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expectedDegreesOfFreedom = 3.0;
        
        double actualDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        
        assertEquals(expectedDegreesOfFreedom, actualDegreesOfFreedom, 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        double expectedMean = 1.5;
        
        double actualMean = fDistribution.getNumericalMean();
        
        assertEquals(expectedMean, actualMean, 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = 6.0;
        
        double actualVariance = fDistribution.getNumericalVariance();
        
        assertEquals(expectedVariance, actualVariance, 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double expectedLowerBound = 0.0;
        
        double actualLowerBound = fDistribution.getSupportLowerBound();
        
        assertEquals(expectedLowerBound, actualLowerBound, 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double expectedUpperBound = Double.POSITIVE_INFINITY;
        
        double actualUpperBound = fDistribution.getSupportUpperBound();
        
        assertEquals(expectedUpperBound, actualUpperBound, 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean expectedIsInclusive = true;
        
        boolean actualIsInclusive = fDistribution.isSupportLowerBoundInclusive();
        
        assertEquals(expectedIsInclusive, actualIsInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean expectedIsInclusive = false;
        
        boolean actualIsInclusive = fDistribution.isSupportUpperBoundInclusive();
        
        assertEquals(expectedIsInclusive, actualIsInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean expectedIsConnected = true;
        
        boolean actualIsConnected = fDistribution.isSupportConnected();
        
        assertEquals(expectedIsConnected, actualIsConnected);
    }
}