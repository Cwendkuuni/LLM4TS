import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(0.5);
 double expected = -0.5 * Math.log(0.5) + 0.5 * Math.log(Math.PI) - 0.25 * Math.log(2.0);
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(1.0);
 expected = 0.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(1.2);
 expected = 0.4058239314454534;
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(2.0);
 expected = Math.log(Math.PI) - 1.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(-1.0);
 assertNaN(result);

 result = Gamma.logGamma(0.0);
 assertNaN(result);
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(0.5, 0.5);
 double expected = Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 1.0);
 expected = 1.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(1.2, 1.5);
 expected = 0.5392569341482325;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(2.0, 2.0);
 expected = 1.0 / Math.sqrt(Math.PI);
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(3.0, 1.0);
 expected = 2.0 / Math.sqrt(Math.PI);
 assertEquals(expected, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 try {
 Gamma.regularizedGammaP(-1.0, 1.0);
 } catch (MathException e) {
 // Expected MathException
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(0.5, 0.5);
 double expected = 1.0 - Math.sqrt(Math.PI) / 2.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 1.0);
 expected = 0.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.2, 1.5);
 expected = 1.0 - 0.5392569341482325;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(2.0, 2.0);
 expected = 1.0 - 1.0 / Math.sqrt(Math.PI);
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(3.0, 1.0);
 expected = 1.0 - 2.0 / Math.sqrt(Math.PI);
 assertEquals(expected, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 try {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 } catch (MathException e) {
 // Expected MathException
 }
 }

 @Test
 public void testRegularizedGammaPMaxIterations() {
 double result = Gamma.regularizedGammaP(1.0, 1.0, 1e-10, 1);
 assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.2, 1.5, 1e-10, 1);
 assertNaN(result);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterationsException() {
 try {
 Gamma.regularizedGammaP(1.2, 1.5, 1e-10, 0);
 } catch (MaxIterationsExceededException e) {
 // Expected MaxIterationsExceededException
 }
 }
}