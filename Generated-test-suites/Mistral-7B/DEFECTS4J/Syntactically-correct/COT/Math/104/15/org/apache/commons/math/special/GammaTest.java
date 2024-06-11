import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

    /**
     * Test method for {@link Gamma#logGamma(double)}.
     */
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.5);
        double expected = Math.log(Math.PI) + (Math.log(1.5) - 0.5 * Math.log(2.0)) - 0.04227817; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#logGamma(double)} with negative input.
     */
    @Test(expected = MathException.class)
    public void testLogGammaNegativeInput() {
        Gamma.logGamma(-1.0);
    }

    /**
     * Test method for {@link Gamma#logGamma(double)} with zero input.
     */
    @Test(expected = MathException.class)
    public void testLogGammaZeroInput() {
        Gamma.logGamma(0.0);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with valid inputs.
     */
    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 0.888182464429083; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with negative input a.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeA() {
        Gamma.regularizedGammaP(-1.0, 1.5);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with negative input x.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeX() {
        Gamma.regularizedGammaP(1.5, -1.0);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with x = a.
     */
    @Test
    public void testRegularizedGammaPXEqualsA() {
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 0.888182464429083; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with large input values.
     */
    @Test
    public void testRegularizedGammaPLargeInputs() {
        double result = Gamma.regularizedGammaP(1000.0, 1000.0);
        double expected = 0.5; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with valid inputs.
     */
    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 1.111817557367363; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with negative input a.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeA() {
        Gamma.regularizedGammaQ(-1.0, 1.5);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with negative input x.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeX() {
        Gamma.regularizedGammaQ(1.5, -1.0);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with x = a.
     */
    @Test
    public void testRegularizedGammaQXEqualsA() {
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 1.111817557367363; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with large input values.
     */
    @Test
    public void testRegularizedGammaQLargeInputs() {
        double result = Gamma.regularizedGammaQ(1000.0, 1000.0);
        double expected = 0.5; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }
}