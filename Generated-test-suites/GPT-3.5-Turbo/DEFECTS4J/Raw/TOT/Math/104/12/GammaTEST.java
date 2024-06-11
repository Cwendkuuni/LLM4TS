
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.special.Gamma;

public class GammaTest {

    @Test(expected = IllegalArgumentException.class)
    public void testLogGamma_NegativeValue() {
        Gamma.logGamma(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogGamma_ZeroValue() {
        Gamma.logGamma(0);
    }

    @Test
    public void testLogGamma_PositiveValue() {
        double result = Gamma.logGamma(5);
        Assert.assertEquals(3.1780538303479453, result, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaP_NegativeA() throws Exception {
        Gamma.regularizedGammaP(-1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaP_ZeroA() throws Exception {
        Gamma.regularizedGammaP(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaP_NegativeX() throws Exception {
        Gamma.regularizedGammaP(2, -1);
    }

    @Test
    public void testRegularizedGammaP_XEqualsZero() throws Exception {
        double result = Gamma.regularizedGammaP(2, 0);
        Assert.assertEquals(0.0, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP_XGreaterThanA() throws Exception {
        double result = Gamma.regularizedGammaP(2, 5);
        Assert.assertEquals(0.8012523801512524, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP_TypicalCase() throws Exception {
        double result = Gamma.regularizedGammaP(2, 2);
        Assert.assertEquals(0.5939941502901615, result, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQ_NegativeA() throws Exception {
        Gamma.regularizedGammaQ(-1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQ_ZeroA() throws Exception {
        Gamma.regularizedGammaQ(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQ_NegativeX() throws Exception {
        Gamma.regularizedGammaQ(2, -1);
    }

    @Test
    public void testRegularizedGammaQ_XEqualsZero() throws Exception {
        double result = Gamma.regularizedGammaQ(2, 0);
        Assert.assertEquals(1.0, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ_XLessThanA() throws Exception {
        double result = Gamma.regularizedGammaQ(2, 1);
        Assert.assertEquals(0.8646647167633873, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ_TypicalCase() throws Exception {
        double result = Gamma.regularizedGammaQ(2, 2);
        Assert.assertEquals(0.4060058497098385, result, 0.0000001);
    }
}
