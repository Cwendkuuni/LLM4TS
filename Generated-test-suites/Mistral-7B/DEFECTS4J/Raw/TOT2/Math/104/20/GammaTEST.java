import org.apache.commons.math.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        Assert.assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        Assert.assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        Assert.assertEquals(Math.log(1.07343668494115146 * Math.PI), Gamma.logGamma(1.5), 1e-10);

        // Test with negative input
        Assert.assertNull(Gamma.logGamma(-1.0));

        // Test with non-numeric input
        Assert.assertNull(Gamma.logGamma("not a number"));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test with valid inputs
        Assert.assertEquals(1.0, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        Assert.assertEquals(0.886226925452758, Gamma.regularizedGammaP(1.5, 1.0), 1e-10);
        Assert.assertEquals(0.668646461424715, Gamma.regularizedGammaP(2.5, 1.0), 1e-10);

        // Test with small x
        Assert.assertEquals(0.975455413280529, Gamma.regularizedGammaP(1.5, 0.01), 1e-10);

        // Test with large x & a close to 1
        Assert.assertEquals(1.0 - 0.668646461424715, Gamma.regularizedGammaQ(1.5, 1.0), 1e-10);

        // Test with large x & a > 1
        Assert.assertEquals(1.0 - 0.668646461424715, Gamma.regularizedGammaQ(2.5, 1.0), 1e-10);

        // Test with large x & a < 1
        Assert.assertEquals(0.668646461424715, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);

        // Test with negative x
        try {
            Gamma.regularizedGammaP(1.5, -1.0);
            Assert.fail();
        } catch (AssertionError e) {
            // expected
        }

        // Test with non-numeric input
        try {
            Gamma.regularizedGammaP(1.5, "not a number");
            Assert.fail();
        } catch (AssertionError e) {
            // expected
        }

        // Test with large a
        try {
            Gamma.regularizedGammaP(10000.0, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test with valid inputs
        Assert.assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
        Assert.assertEquals(0.886226925452758, Gamma.regularizedGammaQ(1.5, 1.0), 1e-10);
        Assert.assertEquals(0.668646461424715, Gamma.regularizedGammaQ(2.5, 1.0), 1e-10);

        // Test with small x
        Assert.assertEquals(1.0 - 0.975455413280529, Gamma.regularizedGammaP(1.5, 0.01), 1e-10);

        // Test with large x & a close to 1
        Assert.assertEquals(1.0 - 1.0 - 0.668646461424715, Gamma.regularizedGammaP(1.5, 1.0), 1e-10);

        // Test with large x & a > 1
        Assert.assertEquals(1.0 - 1.0 - 0.668646461424715, Gamma.regularizedGammaP(2.5, 1.0), 1e-10);

        // Test with large x & a < 1
        Assert.assertEquals(1.0 - 0.668646461424715, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);

        // Test with negative x
        try {
            Gamma.regularizedGammaQ(1.5, -1.0);
            Assert.fail();
        } catch (AssertionError e) {
            // expected
        }

        // Test with non-numeric input
        try {
            Gamma.regularizedGammaQ(1.5, "not a number");
            Assert.fail();
        } catch (AssertionError e) {
            // expected
        }

        // Test with large a
        try {
            Gamma.regularizedGammaQ(10000.0, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }
    }
}