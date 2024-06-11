import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerMinimum() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
 PointValuePair result = optimizer.optimize(function, 1.0, 2.0, GoalType.MINIMIZE);

 assertEquals("Minimum not found", -1.012758256758962, result.getValue(), 0.0001);
 assertEquals("Minimum found at", -1.012758256758962, result.getPoint(), 0.0001);
 }

 @Test
 public void testBrentOptimizerMaximum() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) + 2 * x * Math.pow(Math.E, x);
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
 PointValuePair result = optimizer.optimize(function, 1.0, 2.0, GoalType.MAXIMUM);

 assertEquals("Maximum not found", 1.214265879376198, result.getValue(), 0.0001);
 assertEquals("Maximum found at", 1.214265879376198, result.getPoint(), 0.0001);
 }

 @Test
 public void testBrentOptimizerWithInitialGuess() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4;
 }
 };

 BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
 PointValuePair result = optimizer.optimize(function, 2.0, InitialGuess.THE_LARGEST_DOUBLE);

 assertEquals("Minimum not found", -2.0, result.getValue(), 0.0001);
 assertEquals("Minimum found at", -2.0, result.getPoint(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.1, -1.0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerZeroRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0, null);
 }
}