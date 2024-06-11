import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    // test logGamma method
    
    @Test
    public void testLogGamma_typicalCase() {
        // Typical use case: x > 0
        double x = 5.0;
        double expected = Math.log(24); // log(5!) = log(120) = 4.7874
        
        double actual = Gamma.logGamma(x);
        
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testLogGamma_negativeX() {
        // Error scenario: x is negative
        double x = -1.0;
        
        double actual = Gamma.logGamma(x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }
    
    @Test
    public void testLogGamma_XisZero() {
        // Error scenario: x is zero
        double x = 0.0;
        
        double actual = Gamma.logGamma(x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }
    
    
    // test regularizedGammaP method
    
    @Test
    public void testRegularizedGammaP_typicalCase() throws MathException {
        // Typical use case: a > 0 and x > 0
        double a = 2.0;
        double x = 1.0;
        double expected = 0.6065;
        
        double actual = Gamma.regularizedGammaP(a, x);
        
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testRegularizedGammaP_aisZero() throws MathException {
        // Error scenario: a is zero
        double a = 0.0;
        double x = 2.0;
        
        double actual = Gamma.regularizedGammaP(a, x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }
    
    @Test
    public void testRegularizedGammaP_negativeX() throws MathException {
        // Error scenario: x is negative
        double a = 2.0;
        double x = -1.0;
        
        double actual = Gamma.regularizedGammaP(a, x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }
    
    
    // test regularizedGammaQ method
    
    @Test
    public void testRegularizedGammaQ_typicalCase() throws MathException {
        // Typical use case: a > 0 and x > 0
        double a = 2.0;
        double x = 1.0;
        double expected = 0.3934;
        
        double actual = Gamma.regularizedGammaQ(a, x);
        
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testRegularizedGammaQ_aisZero() throws MathException {
        // Error scenario: a is zero
        double a = 0.0;
        double x = 2.0;
        
        double actual = Gamma.regularizedGammaQ(a, x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }
    
    @Test
    public void testRegularizedGammaQ_negativeX() throws MathException {
        // Error scenario: x is negative
        double a = 2.0;
        double x = -1.0;
        
        double actual = Gamma.regularizedGammaQ(a, x);
        
        Assert.assertTrue(Double.isNaN(actual));
    }

}