import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        Assert.assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 0.5);
        Assert.assertEquals(0.9544997361036416, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 0.5);
        Assert.assertEquals(0.04550026389635842, result, 1e-9);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() throws MathException {
        double result = Gamma.regularizedGammaP(-2.0, 0.5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() throws MathException {
        double result = Gamma.regularizedGammaQ(-2.0, 0.5);
    }
}