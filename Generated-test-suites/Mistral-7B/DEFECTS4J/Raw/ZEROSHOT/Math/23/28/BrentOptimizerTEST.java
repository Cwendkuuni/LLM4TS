import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 final BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6);

 // Test with a simple function.
 FunctionFunction function = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 1.0;
 }
 };

 UnivariateOptimizer result = optimizer.optimize(function, initialGuess);
 assertNotNull(result);
 assertEquals(-1.4142135625, result.getPoint(), 1e-6);

 // Test with a complex function.
 function = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.log(Math.abs(Math.sin(x) - Math.exp(-x)));
 }
 };

 initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 0.5;
 }
 };

 result = optimizer.optimize(function, initialGuess);
 assertNotNull(result);
 assertEquals(1.5651875749, result.getPoint(), 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerInvalidRelativeThreshold() {
 final BrentOptimizer optimizer = new BrentOptimizer(1.0E-8, 1.0E-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerInvalidAbsoluteThreshold() {
 final BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, -1.0E-6);
 }

 @Test
 public void testBrentOptimizerNoConvergenceChecker() {
 final BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6);

 FunctionFunction function = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.abs(x - 2);
 }
 };

 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 1.0;
 }
 };

 optimizer.optimize(function, initialGuess);
 }

 @Test
 public void testBrentOptimizerWithConvergenceChecker() {
 final BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new MyConvergenceChecker());

 FunctionFunction function = new FunctionFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 1.0;
 }
 };

 optimizer.optimize(function, initialGuess);
 }
}

class FunctionFunction {
 double value(double x) {
 return 0;
 }
}

class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return false;
 }
}