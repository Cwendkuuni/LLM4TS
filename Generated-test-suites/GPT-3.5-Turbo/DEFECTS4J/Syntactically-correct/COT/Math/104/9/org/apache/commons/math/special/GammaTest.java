
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x = 5.0;
        double expected = Math.log(SpecialMathFunctions.gamma(x));
        double actual = Gamma.logGamma(x);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double a = 2.0;
        double x = 5.0;
        double expected = SpecialMathFunctions.regularizedGammaP(a, x);
        double actual = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double a = 2.0;
        double x = 5.0;
        double expected = SpecialMathFunctions.regularizedGammaQ(a, x);
        double actual = Gamma.regularizedGammaQ(a, x);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPFail() throws MathException {
        double a = -2.0;
        double x = 5.0;
        Gamma.regularizedGammaP(a, x);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQFail() throws MathException {
        double a = -2.0;
        double x = 5.0;
        Gamma.regularizedGammaQ(a, x);
    }
}
