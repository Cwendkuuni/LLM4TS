import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        Assert.assertEquals(3.1780538303479453, Gamma.logGamma(5.0), 0.0001);
        
        // Edge case: x = 0
        Assert.assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        
        // Error scenario: x is negative
        Assert.assertEquals(Double.NaN, Gamma.logGamma(-5.0), 0.0001);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        Assert.assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1.0, 1.0), 0.0001);
        
        // Edge case: a = 0
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0001);
        
        // Error scenario: a is negative
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.0001);
        
        // Error scenario: x is negative
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -1.0), 0.0001);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        Assert.assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1.0, 1.0), 0.0001);
        
        // Edge case: a = 0
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 1.0), 0.0001);
        
        // Error scenario: a is negative
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 0.0001);
        
        // Error scenario: x is negative
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, -1.0), 0.0001);
    }
}