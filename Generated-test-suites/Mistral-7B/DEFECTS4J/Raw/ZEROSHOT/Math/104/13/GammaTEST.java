import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double[] testCases = {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
 for (double x : testCases) {
 double result = Gamma.logGamma(x);
 double expected = Math.log(factorial(x));
 assertEquals("Gamma log incorrect for x = " + x, expected, result, 1e-9);
 }
 }

 @Test
 public void testRegularizedGammaP() {
 double[] testCases = {0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
 for (double x : testCases) {
 double result = Gamma.regularizedGammaP(x);
 double expected = psi(x) + Math.E;
 assertEquals("Regularized gamma P incorrect for x = " + x, expected, result, 1e-7);
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 double[] testCases = {0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 10.0, 11.0};
 for (double x : testCases) {
 double result = Gamma.regularizedGammaQ(x);
 double expected = 1.0 - psi(x + 1.0);
 assertEquals("Regularized gamma Q incorrect for x = " + x, expected, result, 1e-7);
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaException() {
 double[] testCases = {-1.0, 0.0, 1.0, Double.NaN};
 for (double x : testCases) {
 Gamma.regularizedGammaP(x, 0.1, 10000);
 }
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaMaxIterations() {
 double[] testCases = {0.5, 0.9, 1.5, 2.5};
 for (double x : testCases) {
 Gamma.regularizedGammaP(0.5, x, 1e-9, 1);
 }
 }

 private static long factorial(double n) {
 long result = 1;
 for (long i = 2; i <= (long) n; i++) {
 result *= i;
 }
 return result;
 }

 private static double psi(double x) {
 if (x == 0.0 || x == 1.0) {
 return 0.0;
 } else {
 return Gamma.logGamma(x + 1.0) - Gamma.logGamma(x);
 }
 }
}