import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.LinearFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

 @Test
 public void testEmptyConstructor() {
 BrentOptimizer optimizer = new BrentOptimizer();
 assertNotNull(optimizer);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeAbsoluteThreshold() {
 new BrentOptimizer(-0.1, 0.0, null);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithSmallRelativeThreshold() {
 new BrentOptimizer(0.01, 0.0, null);
 }

 @Test
 public void testConstructorWithValidArguments() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5, null);
 assertNotNull(optimizer);
 }

 @Test
 public void testConstructorWithCheckers() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-3, 1.0e-5, new MaxEval(100));
 assertNotNull(optimizer);
 }

 @Test
 public void testOptimizeWithFunctionMinimization() throws Exception {
 UnivariateFunction function = new LinearFunction(1.0, 2.0);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 UnivariatePointValuePair solution = optimizer.optimize(function, 0.0, 3.0);
 assertEquals(1.5, solution.getPoint(), 1.0e-6);
 assertEquals(-1.5, solution.getValue(), 1.0e-6);
 }

 @Test
 public void testOptimizeWithFunctionMaximization() throws Exception {
 UnivariateFunction function = new LinearFunction(1.0, 2.0);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 UnivariatePointValuePair solution = optimizer.optimize(function, 0.0, 3.0);
 assertEquals(4.5, solution.getPoint(), 1.0e-6);
 assertEquals(4.5, solution.getValue(), 1.0e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeWithInvalidInterval() {
 UnivariateFunction function = new LinearFunction(1.0, 2.0);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 optimizer.setGoalType(GoalType.MINIMIZE);
 try {
 optimizer.optimize(function, 3.0, 0.0);
 } catch (Exception e) {
 throw new IllegalStateException("Should not reach this point.", e);
 }
 }

 @Test
 public void testOptimizeWithFunctionNoConvergence() throws Exception {
 UnivariateFunction function = new LinearFunction(-1.0, x -> Math.sin(x));
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 optimizer.setGoalType(GoalType.MINIMIZE);
 UnivariatePointValuePair solution = optimizer.optimize(function, -10.0, 10.0);
 assertFalse(solution == null);
 assertFalse(solution.getValue() > 1.0e-5);
 }

 @Test
 public void testOptimizeWithFunctionConvergence() throws Exception {
 UnivariateFunction function = new LinearFunction(1.0, 2.0);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-9, 1.0e-9, null);
 optimizer.setGoalType(GoalType.MINIMIZE);
 UnivariatePointValuePair solution = optimizer.optimize(function, 0.0, 3.0);
 assertEquals(1.5, solution.getPoint(), 1.0e-9);
 assertEquals(-1.5, solution.getValue(), 1.0e-9);
 }
}