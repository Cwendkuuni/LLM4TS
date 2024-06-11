import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test NaN input
        Assert.assertTrue(Double.isNaN(Gamma.logGamma(Double.NaN)));

        // Test negative input
        Assert.assertTrue(Double.isNaN(Gamma.logGamma(-1.0)));

        // Test zero input
        Assert.assertTrue(Double.isNaN(Gamma.logGamma(0.0)));

        // Test positive input
        Assert.assertEquals(55.431749287343875, Gamma.logGamma(6.0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test NaN input
        try {
            Gamma.regularizedGammaP(Double.NaN, 1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test negative a
        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test negative x
        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test x = 0
        Assert.assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);

        // Test a > 1 && x > a
        double result1 = Gamma.regularizedGammaP(2.0, 3.0);
        Assert.assertEquals(0.5939941502901615, result1, 0.0);

        // Test a > 1 && x < a
        double result2 = Gamma.regularizedGammaP(2.0, 1.0);
        Assert.assertEquals(0.26424111765711534, result2, 0.0);

        // Test a >= 1 && x = a
        double result3 = Gamma.regularizedGammaP(3.0, 3.0);
        Assert.assertEquals(0.9590413923210933, result3, 0.0);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test NaN input
        try {
            Gamma.regularizedGammaQ(Double.NaN, 1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test negative a
        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test negative x
        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        // Test x = 0
        Assert.assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);

        // Test a < 1 && x < a
        double result1 = Gamma.regularizedGammaQ(0.5, 0.4);
        Assert.assertEquals(0.4546647580527441, result1, 0.0);

        // Test a < 1 && x > a
        double result2 = Gamma.regularizedGammaQ(0.5, 1.0);
        Assert.assertEquals(0.3173105078629141, result2, 0.0);

        // Test a >= 1 && x = a
        double result3 = Gamma.regularizedGammaQ(3.0, 3.0);
        Assert.assertEquals(0.04095860767890668, result3, 0.0);
    }
}