import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid input
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.Gamma(1.5));
 Assert.assertEquals(expected, result, 1e-6);

 // Test invalid input (negative number)
 result = Gamma.logGamma(-1.0);
 Assert.assertFalse(Double.isFinite(result));

 // Test invalid input (zero or less)
 result = Gamma.logGamma(0.0);
 Assert.assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid input
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = Math.Gamma(1.5) / Math.Gamma(1.5 + 0.5);
 Assert.assertEquals(expected, result, 1e-6);

 // Test invalid input (negative a)
 try {
 result = Gamma.regularizedGammaP(-1.0, 1.5);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));

 // Test invalid input (negative x)
 try {
 result = Gamma.regularizedGammaP(1.5, -1.0);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));

 // Test invalid input (x <= a)
 try {
 result = Gamma.regularizedGammaP(1.5, 1.0);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid input
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 1.0 - (Math.Gamma(1.5) / Math.Gamma(1.5 + 0.5));
 Assert.assertEquals(expected, result, 1e-6);

 // Test invalid input (negative a)
 try {
 result = Gamma.regularizedGammaQ(-1.0, 1.5);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));

 // Test invalid input (negative x)
 try {
 result = Gamma.regularizedGammaQ(1.5, -1.0);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));

 // Test invalid input (x <= a)
 try {
 result = Gamma.regularizedGammaQ(1.5, 1.0);
 } catch (Exception e) {
 // expected
 }
 Assert.assertFalse(Double.isFinite(result));
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterations() {
 double result = Gamma.regularizedGammaP(0.5, 1.0, 1e-9, 10);
 }
}