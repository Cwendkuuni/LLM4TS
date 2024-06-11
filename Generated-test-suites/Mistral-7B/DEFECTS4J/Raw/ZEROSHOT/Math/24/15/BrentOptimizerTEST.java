import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.Values;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test with minimal input
 final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);

 // Test with empty interval
 try {
 optimizer.optimize(new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return 0;
 }
 }, -1, 1);
 fail("Expected NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 // Test with converging function
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return x * x;
 }
 };
 UnivariateOptimizer result = optimizer.optimize(function, -5, 5);
 double expected = 0;
 assertEquals(expected, result.getPoint().getX(), 0.001);
 assertEquals(expected, result.getValue(), 0.0, 0.001);

 // Test with non-converging function
 function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return x - Math.sin(x);
 }
 };
 result = optimizer.optimize(function, -10, 10);
 double expectedValue = -3.141592653589793; // Approximate minimum
 double expectedPoint = 3.141592653589793; // Approximate minimum
 assertEquals(expectedValue, result.getValue(), 0.01);
 assertEquals(expectedPoint, result.getPoint().getX(), 0.01);

 // Test with initial point
 ValueInitializer initializer = new ValueInitializer(1.0);
 optimizer = new BrentOptimizer(0.01, 0.001, optimizer.getConvergenceChecker(), initializer);
 result = optimizer.optimize(function, -10, 10);
 assertEquals(expectedPoint, result.getPoint().getX(), 0.01);
 assertEquals(expectedValue, result.getValue(), 0.01);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() {
 // Test with non-converging function and custom convergence checker
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return x - Math.sin(x);
 }
 };
 ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
 return false;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
 try {
 optimizer.optimize(function, -10, 10);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}