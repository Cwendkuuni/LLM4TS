import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.*;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double x = 1.5;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = distribution.cumulativeProbability(x);
        
        assertEquals(0.898515, result, 0.000001);
    }
    
    // Test inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double p = 0.9;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = distribution.inverseCumulativeProbability(p);
        
        assertEquals(3.747513, result, 0.000001);
    }
    
    // Test setNumeratorDegreesOfFreedom method with positive value
    @Test
    public void testSetNumeratorDegreesOfFreedom_positiveValue() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        distribution.setNumeratorDegreesOfFreedom(5.0);
        
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    // Test setNumeratorDegreesOfFreedom method with zero value
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_zeroValue() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        distribution.setNumeratorDegreesOfFreedom(0.0);
    }
    
    // Test setDenominatorDegreesOfFreedom method with positive value
    @Test
    public void testSetDenominatorDegreesOfFreedom_positiveValue() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        distribution.setDenominatorDegreesOfFreedom(6.0);
        
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    // Test setDenominatorDegreesOfFreedom method with zero value
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_zeroValue() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        distribution.setDenominatorDegreesOfFreedom(0.0);
    }
}