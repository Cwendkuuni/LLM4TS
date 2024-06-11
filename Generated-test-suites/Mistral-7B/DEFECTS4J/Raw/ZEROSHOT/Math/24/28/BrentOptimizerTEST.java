import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor
 BrentOptimizer optimizer = new BrentOptimizer(null);

 // Test with specific constructors
 BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.001, null);
 BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001);

 // Test that the optimizer has the correct goal type
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());

 // Test that the optimizer throws exceptions when constructing with invalid arguments
 try {
 new BrentOptimizer(-0.01, 0.0, null);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 new BrentOptimizer(0.0, 0.0, null);
 fail("Expected NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 // Test that the optimizer finds the minimum of a simple function
 Function function = new Function() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };

 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 2.0;
 }
 };

 PointValuePair result = optimizer.optimize(function, initialGuess);
 assertEquals(0.0, result.getPoint(), 0.0001);
 assertEquals(0.0, result.getValue(), 0.0001);

 // Test that the optimizer finds the minimum of a more complex function
 function = new Function() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) * Math.exp(-x);
 }
 };

 initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 1.0;
 }
 };

 result = optimizer.optimize(function, initialGuess);
 assertTrue(result.getValue() < 0);
 assertFalse(Double.isNaN(result.getValue()));
 assertFalse(Double.isInfinite(result.getValue()));
 }

 // Inner class for creating a test function
 abstract static class Function {
 abstract double value(double x);
 }
}