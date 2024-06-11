import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.math.MathException;


public class FDistributionImplTest {
    
    private static FDistributionImpl fDistribution;
    
    @BeforeClass
    public static void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }
    
    // Test cumulativeProbability method
    
    // Typical use case
    @Test
    public void testCumulativeProbability_typical() throws MathException {
        double x = 2.0;
        double expected = 0.8785303871;
        double delta = 0.0000000001;
        
        double result = fDistribution.cumulativeProbability(x);
        
        assertEquals(expected, result, delta);
    }
    
    // Edge case: x = 0
    @Test
    public void testCumulativeProbability_zero() throws MathException {
        double x = 0.0;
        double expected = 0.0;
        
        double result = fDistribution.cumulativeProbability(x);
        
        assertEquals(expected, result, 0.0);
    }
    
    // Error scenario: x < 0
    @Test(expected = MathException.class)
    public void testCumulativeProbability_negativeX() throws MathException {
        double x = -1.0;
        
        fDistribution.cumulativeProbability(x);
    }
    
    // Test inverseCumulativeProbability method
    
    // Typical use case
    @Test
    public void testInverseCumulativeProbability_typical() throws MathException {
        double p = 0.8;
        double expected = 1.3188598344;
        double delta = 0.0000000001;
        
        double result = fDistribution.inverseCumulativeProbability(p);
        
        assertEquals(expected, result, delta);
    }
    
    // Edge case: p = 0
    @Test
    public void testInverseCumulativeProbability_zero() throws MathException {
        double p = 0.0;
        double expected = 0.0;
        
        double result = fDistribution.inverseCumulativeProbability(p);
        
        assertEquals(expected, result, 0.0);
    }
    
    // Edge case: p = 1
    @Test
    public void testInverseCumulativeProbability_one() throws MathException {
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        
        double result = fDistribution.inverseCumulativeProbability(p);
        
        assertEquals(expected, result, 0.0);
    }
    
    // Error scenario: p < 0
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_negativeP() throws MathException {
        double p = -0.5;
        
        fDistribution.inverseCumulativeProbability(p);
    }
    
    // Error scenario: p > 1
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_greaterThanOne() throws MathException {
        double p = 1.5;
        
        fDistribution.inverseCumulativeProbability(p);
    }
    
    // Test getDomainLowerBound method
    
    // Typical use case
    @Test
    public void testGetDomainLowerBound_typical() {
        double p = 0.9;
        double expected = 0.0;
        
        double result = fDistribution.getDomainLowerBound(p);
        
        assertEquals(expected, result, 0.0);
    }
    
    // Test getDomainUpperBound method
    
    // Typical use case
    @Test
    public void testGetDomainUpperBound_typical() {
        double p = 0.9;
        double expected = Double.MAX_VALUE;
        
        double result = fDistribution.getDomainUpperBound(p);
        
        assertEquals(expected, result, 0.0);
    }
    
    // Test getInitialDomain method
    
    // Typical use case
    @Test
    public void testGetInitialDomain_typical() {
        double p = 0.9;
        double expected = 10.0;
        
        double result = fDistribution.getInitialDomain(p);
        
        assertEquals(expected, result, 0.0);
    }
    
}