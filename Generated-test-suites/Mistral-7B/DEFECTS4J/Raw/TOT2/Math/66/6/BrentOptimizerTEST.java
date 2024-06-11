import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVectorValue;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearPointValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateRealFunction testFunction;
 private UnivariateOptimizer optimizer;

 @Before
 public void setup() {
 testFunction = new SimpleFunction();
 optimizer = new BrentOptimizer();
 }

 @Test
 public void testMinimization() {
 PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
 assertEquals(-2.0, result.getPoint(), 1e-7);
 assertEquals(1.5e-15, result.getValue(), 1e-15);
 }

 @Test
 public void testMaximization() {
 PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
 assertEquals(2.0, result.getPoint(), 1e-7);
 assertEquals(1.5e-15, result.getValue(), 1e-15);
 }

 @Test
 public void testMinimizationWithBounds() {
 PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 5.0, 0.0);
 assertEquals(1.5, result.getPoint(), 1e-7);
 assertEquals(1.25e-15, result.getValue(), 1e-15);
 }

 @Test
 public void testMaxIterationsExceeded() {
 optimizer.setMaxIterations(1);
 try {
 optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
 fail();
 } catch (MaxIterationsExceededException e) {
 // Pass
 }
 }

 @Test
 public void testNotStrictlyPositiveException() {
 try {
 optimizer.setMaxEvaluations(-1);
 fail();
 } catch (NotStrictlyPositiveException e) {
 // Pass
 }
 }

 @Test
 public void testObjectiveFunctionThrowsException() {
 FunctionThrowsException function = new FunctionThrowsException();
 try {
 optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
 fail();
 } catch (FunctionEvaluationException e) {
 // Pass
 }
 }

 @Test
 public void testLinearConstraint() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(1.0);
 LinearConstraint constraint = new LinearConstraint(1.0, 2.0, 3.0);
 GoalTypeVectorValue goalType = new GoalTypeVectorValue(GoalType.MINIMIZE);
 PointValuePair result = optimizer.optimize(objeciveFunction, goalType, new SimpleBounds(-10.0, 10.0), constraint);
 assertEquals(0.0, result.getPoint(), 1e-7);
 assertEquals(1.0, result.getValue(), 1e-7);
 }

 @Test
 public void testLinearObjectiveFunctionWithLinearConstraint() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
 LinearConstraint constraint = new LinearConstraint(1.0, 2.0, 3.0);
 GoalTypeVectorValue goalType = new GoalTypeVectorValue(GoalType.MINIMIZE);
 PointValuePair result = optimizer.optimize(objectiveFunction, goalType, new SimpleBounds(-10.0, 10.0), constraint);
 assertEquals(1.5, result.getPoint()[0], 1e-7);
 assertEquals(0.5, result.getPoint()[1], 1e-7);
 assertEquals(-1.5, result.getValue(), 1e-7);
 }

 @Test
 public void testLinearObjectiveFunctionWithLinearConstraintAndLinearBounds() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
 LinearConstraint constraint = new LinearConstraint(1.0, 2.0, 3.0);
 SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
 GoalTypeVectorValue goalType = new GoalTypeVectorValue(GoalType.MINIMIZE);
 PointValuePair result = optimizer.optimize(objectiveFunction, goalType, bounds, constraint);
 assertEquals(1.5, result.getPoint()[0], 1e-7);
 assertEquals(0.5, result.getPoint()[1], 1e-7);
 assertEquals(-1.5, result.getValue(), 1e-7);
 }
}

class SimpleFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2.0;
 }
}

class FunctionThrowsException implements UnivariateRealFunction {
 @Override
 public double value(double x) throws FunctionEvaluationException {
 throw new FunctionEvaluationException();
 }
}