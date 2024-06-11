import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double[] input = {0.1, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
 double[] expected = {0.0453541950554902, 0.5772156649015329, 1.311292684925163, 1.702138360555753, 2.401187407081892, 3.572532873212125, 7.712497364068583};
 for (int i = 0; i < input.length; i++) {
 double result = Gamma.logGamma(input[i]);
 assertTrue("logGamma(" + input[i] + ") != " + expected[i], result == expected[i]);
 }
 }

 @Test
 public void testRegularizedGammaP() {
 double[] inputX = {0.1, 0.5, 1.0, 1.5, 2.0, 3.0, 5.0, 10.0};
 double[] inputA = {0.1, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0};
 double epsilon = 1e-9;
 double[] expected = {0.537831333224126, 0.995268232593316, 0.918288582676672, 0.797884560564656, 0.665393335612347, 0.441469523916415, 0.03572532873212125, 3.474519267215653e-5};
 for (int i = 0; i < inputX.length; i++) {
 double result = Gamma.regularizedGammaP(inputA[i], inputX[i]);
 assertTrue("regularizedGammaP(a=" + inputA[i] + ", x=" + inputX[i] + ") != " + expected[i], result == expected[i]);
 }
 }

 @Test(expected = org.apache.commons.math3.MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterations() {
 double a = 1.0;
 double x = 2.0;
 int maxIterations = 0;
 try {
 Gamma.regularizedGammaP(a, x, 1e-9, maxIterations);
 } catch (MathException e) {
 // expected MathException since maxIterations is 0
 }
 double result = Gamma.regularizedGammaP(a, x, 1e-9, 1);
 assertEquals("regularizedGammaP(a=" + a + ", x=" + x + ") != " + result, result, 1.0 - 0.6366197723675813, 1e-6);
 }

 @Test
 public void testRegularizedGammaQ() {
 double[] inputX = {0.1, 0.5, 1.0, 1.5, 2.0, 3.0, 5.0, 10.0};
 double[] inputA = {0.1, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0};
 double epsilon = 1e-9;
 double[] expected = {0.4621686753955049, 0.994736817371645, 0.918288582676672, 0.797884560564656, 0.665393335612347, 0.441469523916415, 0.9642249649087821, 0.964286079885518};
 for (int i = 0; i < inputX.length; i++) {
 double result = Gamma.regularizedGammaQ(inputA[i], inputX[i]);
 assertTrue("regularizedGammaQ(a=" + inputA[i] + ", x=" + inputX[i] + ") != " + expected[i], result == expected[i]);
 }
 }

}