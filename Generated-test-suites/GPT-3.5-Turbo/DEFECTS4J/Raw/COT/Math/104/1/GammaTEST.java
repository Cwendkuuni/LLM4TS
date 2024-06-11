
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        Assert.assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        Assert.assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(-1), 0.0001);
        Assert.assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0001);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        Assert.assertEquals(0.00000084786, Gamma.regularizedGammaP(0.5, 0.5), 0.000001);
        Assert.assertEquals(0.115329041, Gamma.regularizedGammaP(1, 1), 0.000001);
        Assert.assertEquals(1, Gamma.regularizedGammaP(1, Double.POSITIVE_INFINITY), 0.000001);
    }
        
    @Test
    public void testRegularizedGammaQ() throws MathException {
        Assert.assertEquals(0.999999152, Gamma.regularizedGammaQ(0.5, 0.5), 0.000001);
        Assert.assertEquals(0.884670958, Gamma.regularizedGammaQ(1, 1), 0.000001);
        Assert.assertEquals(0, Gamma.regularizedGammaQ(1, Double.POSITIVE_INFINITY), 0.000001);
    }
}
