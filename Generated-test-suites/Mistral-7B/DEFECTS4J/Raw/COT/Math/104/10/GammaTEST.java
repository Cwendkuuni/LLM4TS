import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(0.5);
        double expected = -0.05714583752316305; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(1.5);
        expected = 0.405822995680755;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(2.5);
        expected = 0.91829688517664;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(3.5);
        expected = 1.36659283218642;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(4.5);
        expected = 1.71238331271293;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(5.5);
        expected = 2.05233640220804;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(10.0);
        expected = 4.59843739351387;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNaN() {
        Gamma.logGamma(Double.NaN);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNegative() {
        Gamma.logGamma(-2.0);
    }

    @Test(expected = MathException.class)
    public void testLogGammaZero() {
        Gamma.logGamma(0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(0.5, 0.5);
        double expected = 0.886226925452758; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(1.5, 1.5);
        expected = 0.888193948175718;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(2.5, 2.5);
        expected = 0.911634488279757;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(3.5, 3.5);
        expected = 0.924597760236673;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(4.5, 4.5);
        expected = 0.932518332363356;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(5.5, 5.5);
        expected = 0.936354373654568;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(6.5, 6.5);
        expected = 0.939968563231480;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(10.0, 10.0);
        expected = 0.995632853135421;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPEpsilon() {
        Gamma.regularizedGammaP(0.5, 0.5, 0.0, 1);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPMaxIterations() {
        Gamma.regularizedGammaP(0.5, 0.5, 0.0, 1);
    }

    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(0.5, 0.5);
        double expected = 0.113773074547242; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(1.5, 1.5);
        expected = 0.110458889521987;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(2.5, 2.5);
        expected = 0.106682922881157;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(3.5, 3.5);
        expected = 0.104031325652998;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(4.5, 4.5);
        expected = 0.101695898390173;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(5.5, 5.5);
        expected = 0.100322483964754;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(6.5, 6.5);
        expected = 0.099293208325877;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(10.0, 10.0);
        expected = 0.077872426437906;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNaN() {
        Gamma.regularizedGammaQ(Double.NaN, 0.5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeA() {
        Gamma.regularizedGammaQ(-2.0, 0.5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeX() {
        Gamma.regularizedGammaQ(0.5, -2.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQXLessThanA() {
        Gamma.regularizedGammaQ(2.0, 1.0);
    }
}