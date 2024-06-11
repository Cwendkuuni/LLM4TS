import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() throws MathException {
 int testCases = 10;
 for (int i = 0; i < testCases; i++) {
 double x = Math.random();
 double expected = Math.log(SpecialFunctions.gamma(i + 1.0));
 double result = Gamma.logGamma(x);
 assertEquals(expected, result, 1e-9);
 }
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 double x = Double.NaN;
 Gamma.logGamma(x);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 double x = -1.0;
 Gamma.logGamma(x);
 }

 @Test
 public void testRegularizedGammaP() throws MathException {
 int testCases = 10;
 for (int i = 0; i < testCases; i++) {
 double a = Math.random();
 double x = Math.random();
 double expected = SpecialFunctions.gammaRegularizedP(a, x);
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-9);
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNaN() {
 double a = Double.NaN;
 double x = 1.0;
 Gamma.regularizedGammaP(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 double a = -1.0;
 double x = 1.0;
 Gamma.regularizedGammaP(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeX() {
 double a = 1.0;
 double x = -1.0;
 Gamma.regularizedGammaP(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPZeroX() {
 double a = 1.0;
 double x = 0.0;
 Gamma.regularizedGammaP(a, x);
 }

 @Test
 public void testRegularizedGammaQ() throws MathException {
 int testCases = 10;
 for (int i = 0; i < testCases; i++) {
 double a = Math.random();
 double x = Math.random();
 double expected = 1.0 - SpecialFunctions.gammaRegularizedP(a, x);
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-9);
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNaN() {
 double a = Double.NaN;
 double x = 1.0;
 Gamma.regularizedGammaQ(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 double a = -1.0;
 double x = 1.0;
 Gamma.regularizedGammaQ(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeX() {
 double a = 1.0;
 double x = -1.0;
 Gamma.regularizedGammaQ(a, x);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQZeroX() {
 double a = 1.0;
 double x = 0.0;
 Gamma.regularizedGammaQ(a, x);
 }

 @Test
 public void testRegularizedGammaPMaxIterations() throws MathException {
 double a = 1.0;
 double x = 1.1;
 double epsilon = 1e-12;
 int maxIterations = 10;
 try {
 Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // Expected
 }
 }
}