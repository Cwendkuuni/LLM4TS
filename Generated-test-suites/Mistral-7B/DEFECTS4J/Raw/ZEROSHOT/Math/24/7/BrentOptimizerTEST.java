import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.SimpleFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test function: f(x) = x^2
 SimpleFunction function = new SimpleFunction("f", x -> Math.pow(x, 2));

 // Test minimization
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
 PointValuePair result = optimizer.optimize(function, 0.0, 1.0);
 assertEquals(0.0, result.getPoint(), 1e-6);
 assertEquals(0.0, result.getValue(), 1e-6);

 // Test maximization
 optimizer = new BrentOptimizer(1e-6, 0.0);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 result = optimizer.optimize(function, 0.0, 1.0);
 assertEquals(1.0, result.getPoint(), 1e-6);
 assertEquals(1.0, result.getValue(), 1e-6);

 // Test with initial guess
 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 2.0;
 }
 };
 optimizer = new BrentOptimizer(1e-6, 0.0);
 result = optimizer.optimize(function, initialGuess);
 assertEquals(1.0, result.getPoint(), 1e-6);
 assertEquals(1.0, result.getValue(), 1e-6);

 // Test convergence checker
 ConvergenceChecker<PointValuePair> convergenceChecker = (iter, previous, current) -> {
 if (iter > 100) {
 throw new MaxEvalExceededException("Maximum number of iterations exceeded");
 }
 return current.getValue() - previous.getValue() < 1e-8;
 };
 optimizer = new BrentOptimizer(1e-6, 0.0, convergenceChecker);
 result = optimizer.optimize(function, 0.0, 1.0);
 assertEquals(Math.sqrt(2.0) / 2.0, result.getPoint(), 1e-6);
 assertEquals(Math.sqrt(2.0) / 4.0, result.getValue(), 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeAbsoluteThreshold() {
 new BrentOptimizer(1e-6, -1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallRelativeThreshold() {
 new BrentOptimizer(1e-8, 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeRelativeThreshold() {
 new BrentOptimizer(-1e-6, 1e-6);
 }
}