import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        double expected = 0.0;
        assertEquals(expected, result, 0.0001);

        result = Gamma.logGamma(2.0);
        expected = Math.log(Math.PI) + Math.log(2.0) - 0.5 * Math.log(2.0);
        assertEquals(expected, result, 0.0001);

        result = Gamma.logGamma(0.5);
        expected = -Math.log(Math.PI) + 0.5 * Math.log(Math.SQRT2) - 0.5;
        assertEquals(expected, result, 0.0001);

        result = Gamma.logGamma(-1.0);
        assertNaN(result);

        result = Gamma.logGamma(0.0);
        assertNaN(result);
    }

    @Test(expected = MathException.class)
    public void testLogGammaThrowsExceptionForNegativeArgument() {
        Gamma.logGamma(-1.0);
    }

    @Test(expected = MathException.class)
    public void testLogGammaThrowsExceptionForNonPositiveArgument() {
        Gamma.logGamma(0.0);
    }

    // Other test cases for regularizedGammaP and regularizedGammaQ methods go here
}