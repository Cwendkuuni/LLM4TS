import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double[] testCases = {0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
 for (double x : testCases) {
 double expected = Math.log(Gamma.gamma(x + 1.0));
 double result = Gamma.logGamma(x);
 assertEquals(expected, result, 1e-12);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLogGammaNegativeArgument() {
 Gamma.logGamma(-1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLogGammaNaNArgument() {
 Gamma.logGamma(Double.NaN);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaNResult() {
 Gamma.logGamma(0.0);
 }

 @Test
 public void testRegularizedGammaP() {
 double[] testCases = {0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
 for (double a : testCases) {
 for (double x : testCases) {
 double expected = Gamma.gamma(a) / Gamma.gamma(a + x);
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-12);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaPAggregateNegativeArgument() {
 Gamma.regularizedGammaP(-0.5, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaPAggregateZeroArgument() {
 Gamma.regularizedGammaP(0.5, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaPAggregateNaNArgument() {
 Gamma.regularizedGammaP(1.5, Double.NaN);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPMaxIterationsExceeded() throws MathException {
 double a = 1.0;
 double x = 100.0;
 int maxIterations = 1;
 Gamma.regularizedGammaP(a, x, 1e-12, maxIterations);
 }

 @Test
 public void testRegularizedGammaQ() {
 double[] testCases = {0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
 for (double a : testCases) {
 for (double x : testCases) {
 double expected = 1.0 - Gamma.gamma(a) / Gamma.gamma(a + x);
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-12);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaQAggregateNegativeArgument() {
 Gamma.regularizedGammaQ(-0.5, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaQAggregateZeroArgument() {
 Gamma.regularizedGammaQ(0.5, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegularizedGammaQAggregateNaNArgument() {
 Gamma.regularizedGammaQ(1.5, Double.NaN);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQMaxIterationsExceeded() throws MathException {
 double a = 1.0;
 double x = 100.0;
 int maxIterations = 1;
 Gamma.regularizedGammaQ(a, x, 1e-12, maxIterations);
 }
}