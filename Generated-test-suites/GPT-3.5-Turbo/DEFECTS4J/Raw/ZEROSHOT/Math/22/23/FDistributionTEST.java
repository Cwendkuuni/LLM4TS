import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class FDistributionTest {
    
    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(10, 10);
        
        // Test values from R package "stats"
        assertEquals(0.000006775294, distribution.density(0.01), EPSILON);
        assertEquals(0.005384986315, distribution.density(0.1), EPSILON);
        assertEquals(0.183424366033, distribution.density(0.5), EPSILON);
        assertEquals(0.2923252687, distribution.density(1.0), EPSILON);
        assertEquals(0.3792728753, distribution.density(2.5), EPSILON);
        assertEquals(0.3213140206, distribution.density(5.0), EPSILON);
        assertEquals(0.2151715118, distribution.density(10.0), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(10, 10);
        
        // Test values from R package "stats"
        assertEquals(0.0000000000000002665046, distribution.cumulativeProbability(0.01), EPSILON);
        assertEquals(0.001539381785, distribution.cumulativeProbability(0.1), EPSILON);
        assertEquals(0.214349465222, distribution.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.3822284045, distribution.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.6225768828, distribution.cumulativeProbability(2.5), EPSILON);
        assertEquals(0.8204486971, distribution.cumulativeProbability(5.0), EPSILON);
        assertEquals(0.951028412, distribution.cumulativeProbability(10.0), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(0, 10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidInverseCumAccuracy() {
        FDistribution distribution = new FDistribution(10, 10, -1);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(10, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(10, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(1, distribution.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(10, 10);
        
        assertEquals(10, distribution.getNumericalVariance(), EPSILON);
    }
}