import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double result = Gamma.logGamma(1.0);
 Assert.assertEquals(0.0, result, 1e-9);

 result = Gamma.logGamma(2.0);
 Assert.assertEquals(Math.log(2.0 * Math.PI), result, 1e-9);

 result = Gamma.logGamma(3.0);
 Assert.assertEquals(Math.log(2.0 * Math.PI) + Math.log(3.0) - 1.0, result, 1e-9);

 // Test invalid inputs
 result = Gamma.logGamma(-1.0);
 Assert.assertNaN(result);

 result = Gamma.logGamma(0.0);
 Assert.assertNaN(result);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs
 double result = Gamma.regularizedGammaP(0.5, 1.0);
 Assert.assertEquals(Math.sqrt(Math.PI) / 2.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 1.0);
 Assert.assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.5, 2.0);
 Assert.assertEquals(0.7237914380275481, result, 1e-9);

 // Test invalid inputs
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 Assert.assertNaN(result);

 result = Gamma.regularizedGammaP(0.0, 1.0);
 Assert.assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 0.0);
 Assert.assertEquals(1.0, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 // Test exception
 double result = Gamma.regularizedGammaP(1.5, 0.5);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs
 double result = Gamma.regularizedGammaQ(0.5, 1.0);
 Assert.assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 1.0);
 Assert.assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.5, 2.0);
 Assert.assertEquals(1.0 - 0.7237914380275481, result, 1e-9);

 // Test invalid inputs
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 Assert.assertNaN(result);

 result = Gamma.regularizedGammaQ(0.0, 1.0);
 Assert.assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 0.0);
 Assert.assertEquals(0.0, result, 1e-9);
 }
}