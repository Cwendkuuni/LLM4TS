import org.apache.commons.math3.MathException;
import org.apache.commons.math3.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double[] testCases = {-1.0, 0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
 for (double x : testCases) {
 double result = Gamma.logGamma(x);
 assertEquals("Log gamma test failed for input: " + x, Math.log(fact(x)), result, 1e-10);
 }
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 double result = Gamma.logGamma(-1.5);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 double result = Gamma.logGamma(-1.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testLogGammaZero() {
 double result = Gamma.logGamma(0.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test
 public void testRegularizedGammaP() {
 double[] testCases = {0.5, 1.0, 2.5, 3.0, 5.0, 10.0};
 for (double a : testCases) {
 for (double x : new double[] {0.0, 1.0, 2.0, 3.0, 5.0, 10.0}) {
 double result = Gamma.regularizedGammaP(a, x);
 double expected = pFact(a, x);
 assertEquals("Regularized gamma p test failed for inputs: a = " + a + ", x = " + x, expected, result, 1e-10);
 }
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNanA() {
 double result = Gamma.regularizedGammaP(-1.5, 2.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNanX() {
 double result = Gamma.regularizedGammaP(1.5, -1.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 double result = Gamma.regularizedGammaP(-1.0, 2.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPZeroX() {
 double result = Gamma.regularizedGammaP(1.0, 0.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test
 public void testRegularizedGammaPZeroA() {
 double result = Gamma.regularizedGammaP(0.0, 1.0);
 assertEquals("Regularized gamma p test failed for inputs: a = 0.0, x = 1.0", 1.0, result, 1e-10);
 }

 @Test
 public void testRegularizedGammaQ() {
 double[] testCases = {0.5, 1.0, 2.5, 3.0, 5.0, 10.0};
 for (double a : testCases) {
 for (double x : new double[] {0.0, 1.0, 2.0, 3.0, 5.0, 10.0}) {
 double result = Gamma.regularizedGammaQ(a, x);
 double expected = 1.0 - pFact(a, x);
 assertEquals("Regularized gamma q test failed for inputs: a = " + a + ", x = " + x, expected, result, 1e-10);
 }
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNaNX() {
 double result = Gamma.regularizedGammaQ(1.5, -1.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 double result = Gamma.regularizedGammaQ(-1.0, 2.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQZeroX() {
 double result = Gamma.regularizedGammaQ(1.0, 0.0);
 fail("Expected MathException, but got: " + result);
 }

 @Test
 public void testRegularizedGammaQZeroA() {
 double result = Gamma.regularizedGammaQ(0.0, 1.0);
 assertEquals("Regularized gamma q test failed for inputs: a = 0.0, x = 1.0", 0.0, result, 1e-10);
 }

 private static double fact(double n) {
 if (n == Math.floor(n)) {
 return Math.pow(2.0, (n - 1.0) * Math.log(n) - n + 1.0 + Eps.GAMMA_EPSILON);
 } else {
 throw new IllegalArgumentException("n must be an integer");
 }
 }

 private static double pFact(double a, double x) {
 double t = a;
 double prod = 1.0;
 while (t < x) {
 prod *= t;
 t += 1.0;
 }
 return prod / gamma(a);
 }

 private static double gamma(double x) {
 if (x == 0.0 || x == 1.0) {
 return 1.0;
 } else if (x <= 0.0) {
 throw new IllegalArgumentException("x must be a positive number");
 } else {
 return Math.exp(Gamma.logGamma(x));
 }
 }

static final double Eps = new Double(1.2e-7);
}