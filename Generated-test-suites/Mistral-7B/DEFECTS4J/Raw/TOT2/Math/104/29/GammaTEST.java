import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.logGamma(2.5);
 assertEquals(Math.log(Math.PI), result, Gamma.DEFAULT_EPSILON);

 result = Gamma.logGamma(1.5);
 assertEquals(Math.log(Math.SQRT2 * Math.PI), result, Gamma.DEFAULT_EPSILON);

 result = Gamma.logGamma(0.5);
 assertEquals(Math.log(Math.SQRT_PI / 2), result, Gamma.DEFAULT_EPSILON);

 result = Gamma.logGamma(-1.0);
 assertNaN(result);

 result = Gamma.logGamma(0.0);
 assertNaN(result);
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(1.5, 1.0);
 assertEquals(0.8862269254527580526, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(1.5, 2.0);
 assertEquals(0.9573983133558591481, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(2.0, 2.0);
 assertEquals(0.6366197723671875098, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(1.5, 3.0);
 assertEquals(0.9885399367864731001, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(2.0, 3.0);
 assertEquals(0.5772453462441633172, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(0.5, 0.5);
 assertEquals(0.5, result, Gamma.DEFAULT_EPSILON);

 result = Gamma.regularizedGammaP(2.5, 0.5);
 assertEquals(1.0 - 0.7853981633974483096, result, Gamma.DEFAULT_EPSILON);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaP_ThrowsMathException() {
 try {
 Gamma.regularizedGammaP(-1.0, 1.0);
 } catch (MathException e) {
 // expected
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaP_MaxIterationsExceededException() {
 try {
 Gamma.regularizedGammaP(1.0, 1000000.0, 1e-15, 100);
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }
}