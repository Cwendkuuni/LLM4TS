import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        double expected = 3.1780538303479458;
        Assert.assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 3.0);
        double expected = 0.3233235838169366;
        Assert.assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        double expected = 0.6766764161830634;
        Assert.assertEquals(expected, result, 0.000001);
    }

}