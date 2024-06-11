import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 double expected = 0.0; // log(1!) = 0
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(2.0);
 expected = Math.log(1.0 / Math.SQUAREROOT_E); // log(2!) = ln(2) - 1/2 * ln(2)
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(5.5);
 expected = Math.log(355.0 / 300.0) - Math.log(11.0 / 4.0) - 1.0 / 4.0; // log(5.5!)
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(-1.0);
 assertTrue(Double.isNaN(result));

 result = Gamma.logGamma(0.0);
 assertTrue(Double.isNaN(result));

 result = Gamma.logGamma(1.5); // not defined for non-integer values
 assertTrue(Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegativeArgument() {
 Gamma.logGamma(-1.0);
 }

 @Test(expected = MathException.class)
 public void testLogGammaZeroArgument() {
 Gamma.logGamma(0.0);
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 double expected = 1.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(1.5, 1.0);
 expected = Math.sqrt(Math.PI) / (Math.Γ(1.5) * 2.0);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(2.5, 1.0);
 expected = Math.sqrt(Math.PI) / (Math.Γ(2.5) * Math.SQRT_2);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(5.5, 1.0);
 expected = Math.exp(logGamma(5.5) - 1.0) / Math.gamma(5.5);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(1.0, 1.5); // not defined for x < a
 assertTrue(Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPAssertNegativeArgument() {
 Gamma.regularizedGammaP(-1.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPAssertZeroArgument() {
 Gamma.regularizedGammaP(0.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPAssertXLessThanA() {
 Gamma.regularizedGammaP(1.0, 0.5);
 }

 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 double expected = 0.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(1.5, 1.0);
 expected = 1.0 - Math.sqrt(Math.PI) / (Math.Γ(1.5) * 2.0);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(2.5, 1.0);
 expected = 1.0 - Math.sqrt(Math.PI) / (Math.Γ(2.5) * Math.SQRT_2);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(5.5, 1.0);
 expected = 1.0 - Math.exp(logGamma(5.5) - 1.0) / Math.gamma(5.5);
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(1.0, 1.5); // not defined for x < a
 assertTrue(Double.isNaN(result));
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQAssertNegativeArgument() {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQAssertZeroArgument() {
 Gamma.regularizedGammaQ(0.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQAssertXLessThanA() {
 Gamma.regularizedGammaQ(1.0, 0.5);
 }
}