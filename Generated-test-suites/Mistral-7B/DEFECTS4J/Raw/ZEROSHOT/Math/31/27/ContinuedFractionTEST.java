import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   return n == 0 ? 1.0 : n % 2 == 0 ? x : -1.0 / (x + n);
  }

  @Override
  protected double getB(int n, double x) {
   return n == 0 ? 0.0 : n % 2 == 0 ? 1.0 : (x + n) / (x + n + 2.0);
  }
 }

 @Test
 public void testSquareRoot() {
  ContinuedFraction cf = new TestContinuedFraction();
  for (int i = 0; i < 10; i++) {
   double x = i * 0.1;
   double value = cf.evaluate(x);
   double expected = FastMath.sqrt(x + 0.25);
   assertEquals("Failed for x = " + x, expected, value, 1e-5);
  }
 }

 @Test(expected = ConvergenceException.class)
 public void testInfinityDivergence() {
  ContinuedFraction cf = new TestContinuedFraction();
  double x = -1.0;
  try {
   cf.evaluate(x, 1e-20, 10000);
  } catch (MaxCountExceededException e) {
   // Max count exceeded is expected here as an alternative to infinite
   // divergence exception
   // ...
  }
  cf.evaluate(x, 1e-20, 100000);
 }

 @Test(expected = ConvergenceException.class)
 public void testNanDivergence() {
  ContinuedFraction cf = new TestContinuedFraction() {
   @Override
   protected double getB(int n, double x) {
    return 1.0 / (x + n);
   }
  };
  double x = -1.0;
  cf.evaluate(x, 1e-20, 10000);
 }

 @Test
 public void testMaxIterations() {
  ContinuedFraction cf = new TestContinuedFraction();
  for (int i = 0; i < 10; i++) {
   double x = i * 0.1;
   double value = cf.evaluate(x, 1e-16, 1);
   double expected = FastMath.sqrt(x + 0.25);
   assertEquals("Failed for x = " + x + " with max iterations = 1", expected, value, 1e-8);
  }
 }

}