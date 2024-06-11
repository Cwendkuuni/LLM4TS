import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.MathException;

public class GammaTest {
    
    private static final double EPSILON = 10e-9;

    @Before
    public void setup() {
        // setup code here
    }

    @Test
    public void testLogGamma() {
        // Typical use case
        double result = Gamma.logGamma(5.0);
        double expected = 3.1780538303479458;
        Assert.assertEquals(expected, result, EPSILON);

        // Edge case
        result = Gamma.logGamma(0.5);
        expected = 0.5723649429247001;
        Assert.assertEquals(expected, result, EPSILON);

        // Error scenario
        result = Gamma.logGamma(Double.NaN);
        Assert.assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        try {
            double result = Gamma.regularizedGammaP(2.0, 3.0);
            double expected = 0.3233235838169366;
            Assert.assertEquals(expected, result, EPSILON);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown");
        }

        // Edge case
        try {
            double result = Gamma.regularizedGammaP(0.5, 0.0);
            double expected = 0.0;
            Assert.assertEquals(expected, result, EPSILON);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown");
        }

        // Error scenario
        try {
            double result = Gamma.regularizedGammaP(Double.NaN, 3.0);
            Assert.fail("Exception should be thrown");
        } catch (MathException e) {
            // expected exception
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        try {
            double result = Gamma.regularizedGammaQ(2.0, 3.0);
            double expected = 0.6766764161830634;
            Assert.assertEquals(expected, result, EPSILON);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown");
        }

        // Edge case
        try {
            double result = Gamma.regularizedGammaQ(0.5, 0.0);
            double expected = 1.0;
            Assert.assertEquals(expected, result, EPSILON);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown");
        }

        // Error scenario
        try {
            double result = Gamma.regularizedGammaQ(Double.NaN, 3.0);
            Assert.fail("Exception should be thrown");
        } catch (MathException e) {
            // expected exception
        }
    }

}