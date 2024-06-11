import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma_typical() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 0.0000001);
    }

    @Test
    public void testLogGamma_zero() {
        double result = Gamma.logGamma(0.0);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testLogGamma_negative() {
        double result = Gamma.logGamma(-5.0);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testRegularizedGammaP_typical() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 1.0);
        assertEquals(0.26424111765711564, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP_largeAandX() throws MathException {
        double result = Gamma.regularizedGammaP(100.0, 100.0);
        assertEquals(0.49999998948388565, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP_aLessThan1() throws MathException {
        double result = Gamma.regularizedGammaP(0.5, 1.0);
        assertEquals(0.682689492137086, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ_typical() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 1.0);
        assertEquals(0.7357588823428844, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ_largeAandX() throws MathException {
        double result = Gamma.regularizedGammaQ(100.0, 100.0);
        assertEquals(0.49999998948388565, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ_xLessThanA() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 1.0);
        assertEquals(0.26424111765711564, result, 0.0000001);
    }

}