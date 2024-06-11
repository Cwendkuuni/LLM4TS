import org.apache.commons.math3.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private static final double TOLERANCE = 1e-8;

 @Test
 public void testBrentOptimizerConstructor() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 assertEquals(1.0e-3, optimizer.getRelativeThreshold(), TOLERANCE);
 assertEquals(1.0e-5, optimizer.getAbsoluteThreshold(), TOLERANCE);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerRelativeThresholdTooSmall() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerAbsoluteThresholdNotPositive() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, -1.0e-5);
 }

 @Test
 public void testBrentOptimizerDoOptimizeBasic() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
 assertEquals(-1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return previous.getValue() == current.getValue();
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0, convergenceChecker);
 assertEquals(-1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeCustomGoalType() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 2);
 }
 };
 GoalType goalType = GoalType.MAXIMIZE;
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0, goalType);
 assertEquals(1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeLoHiSwapped() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return x * (x - 1.0);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 0.0);
 assertEquals(0.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeMax() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 2);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0, GoalType.MAXIMIZE);
 assertEquals(1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeMin() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
 assertEquals(-1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeNegativeStartValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
 assertEquals(-1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeZeroStartValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 2.0);
 assertEquals(-1.0, result.getPoint(), TOLERANCE);
 }

 @Test
 public void testBrentOptimizerDoOptimizeZeroInterval() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 try {
 optimizer.optimize(function, 0.0, 0.0);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBrentOptimizerDoOptimizeNoConvergence() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
 assertNull(result);
 }

}