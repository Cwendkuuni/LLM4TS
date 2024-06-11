import org.junit.Assert;
import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(2.0);
        Assert.assertEquals(0.6931471805599453, result, 1e-9);
        
        result = Gamma.logGamma(1.5);
        Assert.assertEquals(0.5723649429247001, result, 1e-9);
        
        result = Gamma.logGamma(0.5);
        Assert.assertEquals(0.5723649429247001, result, 1e-9);
        
        result = Gamma.logGamma(0.0);
        Assert.assertEquals(Double.NaN, result, 1e-9);
        
        result = Gamma.logGamma(-1.0);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }
    
    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(2.0, 1.0);
        Assert.assertEquals(0.26424111765711536, result, 1e-9);
        
        result = Gamma.regularizedGammaP(1.5, 2.0);
        Assert.assertEquals(0.47693627620446984, result, 1e-9);
        
        result = Gamma.regularizedGammaP(0.5, 0.5);
        Assert.assertEquals(0.15490396878883548, result, 1e-9);
        
        result = Gamma.regularizedGammaP(-1.0, 1.0);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(2.0, 1.0);
        Assert.assertEquals(0.7357588823428847, result, 1e-9);
        
        result = Gamma.regularizedGammaQ(1.5, 2.0);
        Assert.assertEquals(0.5230637237955302, result, 1e-9);
        
        result = Gamma.regularizedGammaQ(0.5, 0.5);
        Assert.assertEquals(0.8450960312111645, result, 1e-9);
        
        result = Gamma.regularizedGammaQ(-1.0, 1.0);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }
}