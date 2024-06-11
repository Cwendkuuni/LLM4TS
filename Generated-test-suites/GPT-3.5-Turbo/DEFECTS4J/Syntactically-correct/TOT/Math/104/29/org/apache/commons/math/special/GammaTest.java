import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma_typicalCase() {
        double result = Gamma.logGamma(5.0); 
        double expected = Math.log(24); // 5!
        Assert.assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testLogGamma_negativeValue() {
        double result = Gamma.logGamma(-2.0);
        Assert.assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP_typicalCase() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 3.0);
        double expected = 0.59399415; // Value calculated using external software
        Assert.assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaP_zeroValue() throws MathException {
        double result = Gamma.regularizedGammaP(3.0, 0.0);
        double expected = 0.0;
        Assert.assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaP_negativeA() throws MathException {
        double result = Gamma.regularizedGammaP(-3.0, 2.0);
        Assert.assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQ_typicalCase() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        double expected = 0.40600585; // Value calculated using external software
        Assert.assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaQ_zeroValue() throws MathException {
        double result = Gamma.regularizedGammaQ(3.0, 0.0);
        double expected = 1.0;
        Assert.assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaQ_negativeA() throws MathException {
        double result = Gamma.regularizedGammaQ(-3.0, 2.0);
        Assert.assertTrue(Double.isNaN(result));
    }

}