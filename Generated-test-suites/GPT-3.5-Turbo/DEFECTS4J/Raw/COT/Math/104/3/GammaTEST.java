import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test logGamma with valid input
        double result = Gamma.logGamma(5.0);
        Assert.assertEquals(3.1780538303479458, result, 0.000000001);

        // Test logGamma with NaN input
        double nanResult = Gamma.logGamma(Double.NaN);
        Assert.assertTrue(Double.isNaN(nanResult));

        // Test logGamma with negative input
        double negativeResult = Gamma.logGamma(-5.0);
        Assert.assertTrue(Double.isNaN(negativeResult));

        // Test logGamma with zero input
        double zeroResult = Gamma.logGamma(0.0);
        Assert.assertTrue(Double.isNaN(zeroResult));

        // Test logGamma with positive infinity input
        double infinityResult = Gamma.logGamma(Double.POSITIVE_INFINITY);
        Assert.assertTrue(Double.isInfinite(infinityResult));

        // Test logGamma with negative infinity input
        double negativeInfinityResult = Gamma.logGamma(Double.NEGATIVE_INFINITY);
        Assert.assertTrue(Double.isInfinite(negativeInfinityResult));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test regularizedGammaP with valid input
        double result = Gamma.regularizedGammaP(5.0, 2.0);
        Assert.assertEquals(0.8089278171233823, result, 0.000000001);

        // Test regularizedGammaP with invalid input
        double nanResult = Gamma.regularizedGammaP(Double.NaN, 2.0);
        Assert.assertTrue(Double.isNaN(nanResult));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test regularizedGammaQ with valid input
        double result = Gamma.regularizedGammaQ(5.0, 2.0);
        Assert.assertEquals(0.19107218287661784, result, 0.000000001);

        // Test regularizedGammaQ with invalid input
        double nanResult = Gamma.regularizedGammaQ(Double.NaN, 2.0);
        Assert.assertTrue(Double.isNaN(nanResult));
    }

}