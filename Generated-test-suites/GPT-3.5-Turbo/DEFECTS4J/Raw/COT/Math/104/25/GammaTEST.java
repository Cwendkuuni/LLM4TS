
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        Assert.assertEquals(3.1780538303479458, result, 0.00001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(3.0, 2.0);
        Assert.assertEquals(0.3233235838169366, result, 0.00001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(3.0, 2.0);
        Assert.assertEquals(0.6766764161830634, result, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPLargeIterations() throws MathException {
        Gamma.regularizedGammaP(3.0, 2.0, 10e-9, 1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQLargeIterations() throws MathException {
        Gamma.regularizedGammaQ(3.0, 2.0, 10e-9, 1);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPInvalid() throws MathException {
        Gamma.regularizedGammaP(-3.0, 2.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQInvalid() throws MathException {
        Gamma.regularizedGammaQ(-3.0, 2.0);
    }

}
