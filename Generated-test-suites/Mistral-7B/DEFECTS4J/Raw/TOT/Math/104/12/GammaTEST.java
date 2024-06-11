import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 double expected = 0.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(2.0);
 expected = Math.log(Math.PI) + Math.log(2.0) - 0.5 * Math.log(2.0);
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(1.5);
 expected = -0.1644934101257231 + Math.log(Math.PI) + Math.log(1.5);
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(0.5);
 expected = -Math.log(2.0) + 0.5 * Math.log(Math.PI);
 assertEquals(expected, result, 1e-10);

 result = Gamma.logGamma(-1.0);
 assertEquals(Double.NaN, result);

 result = Gamma.logGamma(0.0);
 assertEquals(Double.NaN, result);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegativeArgument() {
 Gamma.logGamma(-1.0);
 }

 @Test(expected = MathException.class)
 public void testLogGammaZero() {
 Gamma.logGamma(0.0);
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 double expected = 0.5;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(0.5, 1.0);
 expected = Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(2.0, 1.0);
 expected = 1.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(3.0, 2.0);
 expected = Math.E / Math.SQRT12;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaP(1.0, 2.0);
 expected = 0.5 * Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-10);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeArgument() {
 Gamma.regularizedGammaP(-1.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPZeroArgument() {
 Gamma.regularizedGammaP(0.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPInvalidX() {
 Gamma.regularizedGammaP(1.0, -1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPMaxIterationsExceeded() {
 try {
 Gamma.regularizedGammaP(1.0, 1.0, 1e-20, 1);
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 double expected = 0.5;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(0.5, 1.0);
 expected = 1.0 - Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(2.0, 1.0);
 expected = 1.0;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(3.0, 2.0);
 expected = 1.0 - Math.E / Math.SQRT12;
 assertEquals(expected, result, 1e-10);

 result = Gamma.regularizedGammaQ(1.0, 2.0);
 expected = 1.0 - 0.5 * Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-10);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeArgument() {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQZeroArgument() {
 Gamma.regularizedGammaQ(0.0, 1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQInvalidX() {
 Gamma.regularizedGammaQ(1.0, -1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQMaxIterationsExceeded() {
 try {
 Gamma.regularizedGammaQ(1.0, 1.0, 1e-20, 1);
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }
}