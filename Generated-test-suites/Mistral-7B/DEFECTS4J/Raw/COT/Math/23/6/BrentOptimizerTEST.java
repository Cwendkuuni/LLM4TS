import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

 @Test
 public void testConstructorWithRelativeAndAbsoluteThresholdAndChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 assertEquals(0.01, optimizer.getRelativeThreshold(), 0.00001);
 assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.00001);
 assertEquals(null, optimizer.getConvergenceChecker());
 }

 @Test
 public void testConstructorWithRelativeAndAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 assertEquals(0.01, optimizer.getRelativeThreshold(), 0.00001);
 assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.00001);
 assertEquals(null, optimizer.getConvergenceChecker());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithTooSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
 }

 @Test
 public void testOptimizeMethodWithGoalTypeMinimize() throws Exception {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
 }
 };
 OptimizationResult result = optimizer.optimize(function, GoalType.MINIMIZE);
 assertEquals(-1.7321, result.getPoint().getValue(), 0.001);
 }

 @Test
 public void testOptimizeMethodWithGoalTypeMaximize() throws Exception {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
 }
 };
 OptimizationResult result = optimizer.optimize(function, GoalType.MAXIMIZE);
 assertEquals(1.7321, result.getPoint().getValue(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeMethodWithInvalidGoalType() throws Exception {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return 0.0;
 }
 };
 optimizer.optimize(function, new GoalType("INVALID"));
 }

}