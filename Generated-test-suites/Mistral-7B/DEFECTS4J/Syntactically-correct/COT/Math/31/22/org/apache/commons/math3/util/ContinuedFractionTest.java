import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return (n % 2 == 0) ? 1.0 / (2.0 * x + n) : (n + 1.0) / (2.0 * x + n);
 }

 @Override
 protected double getB(int n, double x) {
 return -(n % 2 == 0) ? 1.0 / (2.0 * x + n) : 1.0;
 }

 @Override
 public double evaluate(double x, double epsilon, int maxIterations) {
 return super.evaluate(x, epsilon, maxIterations);
 }
 };

 @Test
 public void testEvaluateWithDefaultParameters() {
 double result = continuedFraction.evaluate(1.0);
 double expected = 3.0;
 assertEquals(expected, result, 0.00001);
 }

 @Test
 public void testEvaluateWithCustomErrorAndMaxIterations() {
 double result = continuedFraction.evaluate(1.0, 1e-10, 10);
 double expected = 3.1415926536;
 assertEquals(expected, result, 1e-10);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateWithInfiniteValues() {
 continuedFraction.evaluate(0.0, 1e-10, 10);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateWithMaxIterationsReached() {
 continuedFraction.evaluate(1.0, 1e-10, 1);
 }

 @Test
 public void testEvaluateWithNegativeX() {
 double result = continuedFraction.evaluate(-1.0, 1e-10, 10);
 double expected = -3.0;
 assertEquals(expected, result, 0.00001);
 }

}