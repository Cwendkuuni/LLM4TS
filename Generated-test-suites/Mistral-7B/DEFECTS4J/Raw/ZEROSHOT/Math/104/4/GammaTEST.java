import org.apache.commons.math3.Complex;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test with valid inputs
 double x = 1.5;
 double expectedLogGamma = 0.57088595635265835;
 double actualLogGamma = Gamma.logGamma(x);
 assertEquals("logGamma for x = " + x, expectedLogGamma, actualLogGamma, 1e-8);

 x = 0.5;
 expectedLogGamma = -0.03665347927717219;
 actualLogGamma = Gamma.logGamma(x);
 assertEquals("logGamma for x = " + x, expectedLogGamma, actualLogGamma, 1e-8);

 x = 0;
 double NaN = Double.NaN;
 actualLogGamma = Gamma.logGamma(x);
 assertEquals("logGamma for x = 0", NaN, actualLogGamma);

 // Test with negative input
 x = -1.5;
 actualLogGamma = Gamma.logGamma(x);
 assertFalse("logGamma for x = " + x + " should be NaN", Double.isFinite(actualLogGamma));

 // Test with non-numeric input
 x = 'a';
 actualLogGamma = Gamma.logGamma(x);
 assertFalse("logGamma for x = " + x + " should be NaN", Double.isFinite(actualLogGamma));
 }

 @Test
 public void testRegularizedGammaP() {
 // Test with valid inputs
 double a = 1.5;
 double x = 1.5;
 double expectedRegularizedGammaP = 0.4128689452324740;
 double actualRegularizedGammaP = 0;
 try {
 actualRegularizedGammaP = Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 fail("Unexpected MathException when testing regularizedGammaP: " + e.getMessage());
 }
 assertEquals("regularizedGammaP for a = " + a + " and x = " + x, expectedRegularizedGammaP, actualRegularizedGammaP, 1e-8);

 a = 0.5;
 x = 1.0;
 expectedRegularizedGammaP = 0.617276696385645;
 actualRegularizedGammaP = 0;
 try {
 actualRegularizedGammaP = Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 fail("Unexpected MathException when testing regularizedGammaP: " + e.getMessage());
 }
 assertEquals("regularizedGammaP for a = " + a + " and x = " + x, expectedRegularizedGammaP, actualRegularizedGammaP, 1e-8);

 a = 0;
 x = 1.0;
 expectedRegularizedGammaP = 1.0;
 actualRegularizedGammaP = Gamma.regularizedGammaP(a, x);
 assertEquals("regularizedGammaP for a = " + a + " and x = " + x, expectedRegularizedGammaP, actualRegularizedGammaP, 1e-8);

 // Test with non-numeric input
 char invalidInput = 'a';
 try {
 Gamma.regularizedGammaP(a, 0.5);
 fail("MathException should be thrown for non-numeric input");
 } catch (MathException e) {
 // Expected
 }

 // Test with negative a
 a = -1.5;
 x = 1.5;
 try {
 actualRegularizedGammaP = Gamma.regularizedGammaP(a, x);
 fail("MathException should be thrown for negative a");
 } catch (MathException e) {
 // Expected
 }

 // Test with complex input
 Complex input = new Complex(2.5, 1.2);
 try {
 actualRegularizedGammaP = Gamma.regularizedGammaP(input.getRealPart(), input.getImaginaryPart());
 fail("MathException should be thrown for complex input");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test with valid inputs
 double a = 1.5;
 double x = 1.5;
 double expectedRegularizedGammaQ = 0.587811932111024;
 double actualRegularizedGammaQ = 0;
 try {
 actualRegularizedGammaQ = Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 fail("Unexpected MathException when testing regularizedGammaQ: " + e.getMessage());
 }
 assertEquals("regularizedGammaQ for a = " + a + " and x = " + x, expectedRegularizedGammaQ, actualRegularizedGammaQ, 1e-8);

 a = 0.5;
 x = 1.0;
 expectedRegularizedGammaQ = 0.382746310425582;
 actualRegularizedGammaQ = 0;
 try {
 actualRegularizedGammaQ = Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 fail("Unexpected MathException when testing regularizedGammaQ: " + e.getMessage());
 }
 assertEquals("regularizedGammaQ for a = " + a + " and x = " + x, expectedRegularizedGammaQ, actualRegularizedGammaQ, 1e-8);

 a = 0;
 x = 1.0;
 expectedRegularizedGammaQ = 1.0 - expectedRegularizedGammaP(a, x);
 actualRegularizedGammaQ = Gamma.regularizedGammaQ(a, x);
 assertEquals("regularizedGammaQ for a = " + a + " and x = " + x, expectedRegularizedGammaQ, actualRegularizedGammaQ, 1e-8);

 // Test with non-numeric input
 char invalidInput = 'a';
 try {
 Gamma.regularizedGammaQ(a, 0.5);
 fail("MathException should be thrown for non-numeric input");
 } catch (MathException e) {
 // Expected
 }

 // Test with negative a
 a = -1.5;
 x = 1.5;
 try {
 actualRegularizedGammaQ = Gamma.regularizedGammaQ(a, x);
 fail("MathException should be thrown for negative a");
 } catch (MathException e) {
 // Expected
 }

 // Test with complex input
 Complex input = new Complex(2.5, 1.2);
 try {
 actualRegularizedGammaQ = Gamma.regularizedGammaQ(input.getRealPart(), input.getImaginaryPart());
 fail("MathException should be thrown for complex input");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaP_MaxIterations() {
 double a = 0.1;
 double x = 1.1;
 double epsilon = 1e-12;
 int maxIterations = 5;
 Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
 fail("MaxIterationsExceededException should be thrown");
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaQ_MaxIterations() {
 double a = 0.1;
 double x = 1.1;
 double epsilon = 1e-12;
 int maxIterations = 5;
 Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
 fail("MaxIterationsExceededException should be thrown");
 }
}