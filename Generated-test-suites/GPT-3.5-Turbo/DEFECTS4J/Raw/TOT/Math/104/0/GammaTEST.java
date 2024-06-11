import org.junit.Test;
import org.junit.Assert;

public class GammaTest {
    
    // Test logGamma method
    
    @Test
    public void testLogGamma_typicalCase() {
        double result = Gamma.logGamma(5.0);
        double expected = 3.1780538303479458;
        Assert.assertEquals(expected, result, 0.0000001);
    }
    
    @Test
    public void testLogGamma_zeroInput() {
        double result = Gamma.logGamma(0.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testLogGamma_negativeInput() {
        double result = Gamma.logGamma(-2.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    // Test regularizedGammaP method
    
    @Test
    public void testRegularizedGammaP_typicalCase() {
        double result = Gamma.regularizedGammaP(2.0, 1.0);
        double expected = 0.5939941502901612;
        Assert.assertEquals(expected, result, 0.0000001);
    }
    
    @Test
    public void testRegularizedGammaP_zeroA() {
        double result = Gamma.regularizedGammaP(0.0, 1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP_negativeA() {
        double result = Gamma.regularizedGammaP(-2.0, 1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP_zeroX() {
        double result = Gamma.regularizedGammaP(2.0, 0.0);
        double expected = 0.0;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP_negativeX() {
        double result = Gamma.regularizedGammaP(2.0, -1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP_largeIterations() {
        double result = Gamma.regularizedGammaP(2.0, 1.0, 1e-15, 1000000);
        double expected = 0.5939941502901612;
        Assert.assertEquals(expected, result, 0.0000001);
    }
    
    // Test regularizedGammaQ method
    
    @Test
    public void testRegularizedGammaQ_typicalCase() {
        double result = Gamma.regularizedGammaQ(2.0, 1.0);
        double expected = 0.4060058497098388;
        Assert.assertEquals(expected, result, 0.0000001);
    }
    
    @Test
    public void testRegularizedGammaQ_zeroA() {
        double result = Gamma.regularizedGammaQ(0.0, 1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ_negativeA() {
        double result = Gamma.regularizedGammaQ(-2.0, 1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ_zeroX() {
        double result = Gamma.regularizedGammaQ(2.0, 0.0);
        double expected = 1.0;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ_negativeX() {
        double result = Gamma.regularizedGammaQ(2.0, -1.0);
        double expected = Double.NaN;
        Assert.assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ_largeIterations() {
        double result = Gamma.regularizedGammaQ(2.0, 1.0, 1e-15, 1000000);
        double expected = 0.4060058497098388;
        Assert.assertEquals(expected, result, 0.0000001);
    }
}