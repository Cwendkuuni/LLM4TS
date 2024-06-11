import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.special.Gamma.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        Assert.assertEquals(0.0, logGamma(0.0), 1e-9);
        Assert.assertEquals(0.57721566490153286, logGamma(1.0), 1e-9);
        Assert.assertEquals(1.4603160393128883, logGamma(2.0), 1e-9);
        Assert.assertEquals(1.7014118741143335, logGamma(3.0), 1e-9);
        Assert.assertEquals(3.015128225553262, logGamma(5.0), 1e-9);
        Assert.assertEquals(5.73966946328495, logGamma(7.0), 1e-9);
        Assert.assertEquals(11.031611415374614, logGamma(10.0), 1e-9);
        Assert.assertEquals(11.570796326794896, logGamma(11.0), 1e-9);
        Assert.assertEquals(17.814776841332296, logGamma(15.0), 1e-9);
        Assert.assertEquals(21.00121672345166, logGamma(17.0), 1e-9);
        Assert.assertEquals(33.420225406322855, logGamma(20.0), 1e-9);
        Assert.assertEquals(Inf.doubleValue(), logGamma(Inf.doubleValue()), 1e-9);

        try {
            logGamma(-1.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            logGamma(0.5);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        Assert.assertEquals(0.0, regularizedGammaP(0.0, 0.0), 1e-9);
        Assert.assertEquals(0.57721566490153286, regularizedGammaP(1.0, 1.0), 1e-9);
        Assert.assertEquals(0.82210526044182945, regularizedGammaP(1.5, 1.0), 1e-9);
        Assert.assertEquals(0.9590812417744291, regularizedGammaP(2.0, 1.0), 1e-9);
        Assert.assertEquals(0.9821593451625443, regularizedGammaP(3.0, 1.0), 1e-9);
        Assert.assertEquals(0.9943359277524033, regularizedGammaP(4.0, 1.0), 1e-9);
        Assert.assertEquals(0.999262546771151, regularizedGammaP(5.0, 1.0), 1e-9);
        Assert.assertEquals(0.999931722202773, regularizedGammaP(6.0, 1.0), 1e-9);
        Assert.assertEquals(1.0, regularizedGammaP(7.0, 1.0), 1e-9);

        Assert.assertEquals(0.999223582725505, regularizedGammaP(3.0, 4.0), 1e-9);
        Assert.assertEquals(0.999906694223276, regularizedGammaP(5.0, 5.0), 1e-9);
        Assert.assertEquals(0.999970411037365, regularizedGammaP(6.0, 6.0), 1e-9);
        Assert.assertEquals(1.0, regularizedGammaP(7.0, 7.0), 1e-9);

        Assert.assertEquals(0.5, regularizedGammaP(1.0, 1.0), 1e-9);
        Assert.assertEquals(0.25, regularizedGammaP(0.5, 1.0), 1e-9);
        Assert.assertEquals(0.125, regularizedGammaP(0.25, 1.0), 1e-9);
        Assert.assertEquals(0.0625, regularizedGammaP(0.125, 1.0), 1e-9);
        Assert.assertEquals(0.03125, regularizedGammaP(0.0625, 1.0), 1e-9);

        try {
            regularizedGammaP(-1.0, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            regularizedGammaP(0.5, 0.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        Assert.assertEquals(1.0, regularizedGammaQ(0.0, 0.0), 1e-9);
        Assert.assertEquals(0.57721566490153286, 1.0 - regularizedGammaP(1.0, 1.0), 1e-9);
        Assert.assertEquals(0.82210526044182945, 1.0 - regularizedGammaP(1.5, 1.0), 1e-9);
        Assert.assertEquals(0.9590812417744291, 1.0 - regularizedGammaP(2.0, 1.0), 1e-9);
        Assert.assertEquals(0.9821593451625443, 1.0 - regularizedGammaP(3.0, 1.0), 1e-9);
        Assert.assertEquals(0.9943359277524033, 1.0 - regularizedGammaP(4.0, 1.0), 1e-9);
        Assert.assertEquals(0.999262546771151, 1.0 - regularizedGammaP(5.0, 1.0), 1e-9);
        Assert.assertEquals(1.0, 1.0 - regularizedGammaP(6.0, 1.0), 1e-9);

        Assert.assertEquals(0.5, 1.0 - regularizedGammaP(1.0, 1.0), 1e-9);
        Assert.assertEquals(0.75, 1.0 - regularizedGammaP(1.5, 1.0), 1e-9);
        Assert.assertEquals(0.875, 1.0 - regularizedGammaP(2.0, 1.0), 1e-9);
        Assert.assertEquals(0.9375, 1.0 - regularizedGammaP(3.0, 1.0), 1e-9);
        Assert.assertEquals(0.96875, 1.0 - regularizedGammaP(4.0, 1.0), 1e-9);
        Assert.assertEquals(0.984375, 1.0 - regularizedGammaP(5.0, 1.0), 1e-9);
        Assert.assertEquals(0.9921875, 1.0 - regularizedGammaP(6.0, 1.0), 1e-9);

        try {
            regularizedGammaQ(-1.0, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            regularizedGammaQ(0.5, 0.0);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }
    }
}