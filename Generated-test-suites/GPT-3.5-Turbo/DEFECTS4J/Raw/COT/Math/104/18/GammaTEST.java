import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test positive input
        double result = Gamma.logGamma(5);
        Assert.assertEquals(3.1780538303479458, result, 1e-9);

        // Test zero input
        result = Gamma.logGamma(0);
        Assert.assertEquals(Double.NaN, result, 1e-9);

        // Test negative input
        result = Gamma.logGamma(-5);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test positive input
        double result = Gamma.regularizedGammaP(3, 2);
        Assert.assertEquals(0.5939941502901601, result, 1e-9);

        // Test zero input
        result = Gamma.regularizedGammaP(3, 0);
        Assert.assertEquals(0.0, result, 1e-9);

        // Test negative input
        result = Gamma.regularizedGammaP(-3, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);

        // Test exception
        try {
            Gamma.regularizedGammaP(3, 2, 1e-9, 10);
            Assert.fail("Expecting MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            Assert.assertEquals("Maximal iterations count (10) exceeded", e.getMessage());
        }
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test positive input
        double result = Gamma.regularizedGammaQ(3, 2);
        Assert.assertEquals(0.40600584970983993, result, 1e-9);

        // Test zero input
        result = Gamma.regularizedGammaQ(3, 0);
        Assert.assertEquals(1.0, result, 1e-9);

        // Test negative input
        result = Gamma.regularizedGammaQ(-3, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);

        // Test exception
        try {
            Gamma.regularizedGammaQ(3, 2, 1e-9, 10);
            Assert.fail("Expecting MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            Assert.assertEquals("Maximal iterations count (10) exceeded", e.getMessage());
        }
    }

    // 

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testLogGamma_NaN() {
        double result = Gamma.logGamma(Double.NaN);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testLogGamma_NegativeInput() {
        double result = Gamma.logGamma(-10);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP_NaN() throws MathException {
        double result = Gamma.regularizedGammaP(Double.NaN, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP_NegativeParameter() throws MathException {
        double result = Gamma.regularizedGammaP(-3, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP_NegativeValue() throws MathException {
        double result = Gamma.regularizedGammaP(3, -2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP_Exception() throws MathException {
        try {
            Gamma.regularizedGammaP(3, 2, 1e-9, 10);
            Assert.fail("Expecting MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            Assert.assertEquals("Maximal iterations count (10) exceeded", e.getMessage());
        }
    }

    @Test
    public void testRegularizedGammaQ_NaN() throws MathException {
        double result = Gamma.regularizedGammaQ(Double.NaN, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ_NegativeParameter() throws MathException {
        double result = Gamma.regularizedGammaQ(-3, 2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ_NegativeValue() throws MathException {
        double result = Gamma.regularizedGammaQ(3, -2);
        Assert.assertEquals(Double.NaN, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ_Exception() throws MathException {
        try {
            Gamma.regularizedGammaQ(3, 2, 1e-9, 10);
            Assert.fail("Expecting MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            Assert.assertEquals("Maximal iterations count (10) exceeded", e.getMessage());
        }
    }

   // 
}