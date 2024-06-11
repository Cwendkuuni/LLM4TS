import static org.junit.Assert.*;
import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @ org.junit.Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(0.5);
        expected = -0.5 * Math.log(2.0) + 0.5 * Math.log(Math.PI) - 0.25;
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(-1.0);
        assertNaN(result);

        result = Gamma.logGamma(0.0);
        assertNaN(result);
    }

    @ org.junit.Test(expected = MathException.class)
    public void testLogGammaException() {
        Gamma.logGamma(-1.5);
    }

    @ org.junit.Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(0.5, 1.0);
        expected = Math.sqrt(Math.PI) / 2.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.5, 1.0);
        expected = 2.0 / Math.sqrt(Math.PI);
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.0, 5.0);
        expected = 0.621460953026229;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(0.5, 1.5);
        expected = Math.sqrt(Math.PI) / 4.0;
        assertEquals(expected, result, 1e-9);
    }

    @ org.junit.Test(expected = MathException.class)
    public void testRegularizedGammaPException() {
        Gamma.regularizedGammaP(-1.0, 1.0);
    }

    @ org.junit.Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        double expected = 1.0 - 0.621460953026229;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(0.5, 1.0);
        expected = 1.0 - Math.sqrt(Math.PI) / 2.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.5, 1.0);
        expected = 1.0 - 2.0 / Math.sqrt(Math.PI);
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.0, 5.0);
        expected = 1.0 - 0.621460953026229;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(0.5, 1.5);
        expected = 1.0 - Math.sqrt(Math.PI) / 4.0;
        assertEquals(expected, result, 1e-9);
    }

    @ org.junit.Test
    public void testRegularizedGammaPMaxIterations() {
        try {
            double result = Gamma.regularizedGammaP(1.0, 1.0, 0.0, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @ org.junit.Test
    public void testRegularizedGammaQMaxIterations() {
        try {
            double result = Gamma.regularizedGammaQ(1.0, 1.0, 0.0, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}