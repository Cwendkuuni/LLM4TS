import org.apache.commons.math3.SpecialFunctions; // Assuming you are using Apache Commons Math3
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        Assert.assertEquals(0.0, SpecialFunctions.logGamma(0.0), 1e-10);
        Assert.assertEquals(0.0, SpecialFunctions.logGamma(1.0), 1e-10);
        Assert.assertEquals(Math.log(Math.PI), SpecialFunctions.logGamma(0.5), 1e-10);
        Assert.assertEquals(Math.log(1.4436170212522629), SpecialFunctions.logGamma(1.5), 1e-10);
        Assert.assertEquals(Math.log(1.1173745377966261), SpecialFunctions.logGamma(2.0), 1e-10);

        try {
            SpecialFunctions.logGamma(-1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.logGamma(Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.logGamma(Double.NEGATIVE_INFINITY);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testRegularizedGammaP() {
        Assert.assertEquals(0.0, SpecialFunctions.regularizedGammaP(0.0, 0.0), 1e-10);
        Assert.assertEquals(1.0, SpecialFunctions.regularizedGammaP(1.0, 1.0), 1e-10);
        Assert.assertEquals(0.5, SpecialFunctions.regularizedGammaP(0.5, 1.0), 1e-10);
        Assert.assertEquals(0.362686452679517, SpecialFunctions.regularizedGammaP(0.5, 0.5), 1e-10);

        try {
            SpecialFunctions.regularizedGammaP(-1.0, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaP(1.0, -1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaP(1.0, 0.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        Assert.assertEquals(1.0, SpecialFunctions.regularizedGammaQ(0.0, 0.0), 1e-10);
        Assert.assertEquals(0.0, SpecialFunctions.regularizedGammaQ(1.0, 1.0), 1e-10);
        Assert.assertEquals(1.0 - 0.5, SpecialFunctions.regularizedGammaQ(0.5, 1.0), 1e-10);
        Assert.assertEquals(0.637450557653324, SpecialFunctions.regularizedGammaQ(0.5, 0.5), 1e-10);

        try {
            SpecialFunctions.regularizedGammaQ(-1.0, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaQ(1.0, -1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaQ(1.0, 0.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }
    }
}