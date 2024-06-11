import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
    switch (n) {
      case 0:
        return 1.0;
      case 1:
        return x;
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  protected double getB(int n, double x) {
    switch (n) {
      case 0:
        return 0.0;
      case 1:
        return 1.0;
      default:
        return 0.0;
    }
  }
 }

 @Test
 public void testEvaluateSquareRootOfTwo() {
     ContinuedFraction continuedFraction = new TestContinuedFraction();
     double x = 2.0;
     double expected = FastMath.sqrt(2.0);

     assertEquals(expected, continuedFraction.evaluate(x), 1e-6);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateSquareRootOfTwoWithMaxIterations() {
     ContinuedFraction continuedFraction = new TestContinuedFraction();
     double x = 2.0;
     int maxIterations = 2; // less than required for convergence

     continuedFraction.evaluate(x, x, maxIterations);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateSine() {
     ContinuedFraction continuedFraction = new ContinuedFraction() {
         @Override
         protected double getA(int n, double x) {
             return (-1.0).pow(n) / (2.0 * FastMath.PI * FastMath.factorial(2 * n - 1));
         }

         @Override
         protected double getB(int n, double x) {
             return 1.0 / (2.0 * FastMath.PI * FastMath.factorial(2 * n));
         }
     };
     double x = 0.5;
     double epsilon = 1e-14;
     int maxIterations = 50;

     continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateSineWithEpsilonTooSmall() {
     ContinuedFraction continuedFraction = new ContinuedFraction() {
         @Override
         protected double getA(int n, double x) {
             return (-1.0).pow(n) / (2.0 * FastMath.PI * FastMath.factorial(2 * n - 1));
         }

         @Override
         protected double getB(int n, double x) {
             return 1.0 / (2.0 * FastMath.PI * FastMath.factorial(2 * n));
         }
     };
     double x = 0.5;
     double epsilon = 1e-20;
     int maxIterations = 50;

     continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateLogarithmWithMaxIterations() {
     ContinuedFraction continuedFraction = new TestContinuedFraction() {
         @Override
         protected double getA(int n, double x) {
             return 1.0 / (1.0 + n);
         }

         @Override
         protected double getB(int n, double x) {
             return n / (1.0 + n);
         }
     };
     double x = 2.71828;
     double epsilon = 1e-6;
     int maxIterations = 1; // less than required for convergence

     continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 @Test
 public void testEvaluateLogarithm() {
     ContinuedFraction continuedFraction = new TestContinuedFraction() {
         @Override
         protected double getA(int n, double x) {
             return 1.0 / (1.0 + n);
         }

         @Override
         protected double getB(int n, double x) {
             return n / (1.0 + n);
         }
     };
     double x = 2.71828;
     double epsilon = 1e-6;
     int maxIterations = 10;

     assertEquals(x, continuedFraction.evaluate(x, epsilon, maxIterations), 1e-6);
 }
}