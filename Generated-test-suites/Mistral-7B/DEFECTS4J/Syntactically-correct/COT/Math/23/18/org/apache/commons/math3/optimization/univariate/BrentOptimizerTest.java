package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.AbstractUnivariateObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Minimization;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Maximization;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private UnivariateObjectiveFunction objectiveFunction;

 @Before
 public void setUp() {
 objectiveFunction = new MyObjectiveFunction();
 optimizer = new BrentOptimizer(1e-4, 1e-6, null);
 }

 @Test
 public void testConstructors() {
 try {
 new BrentOptimizer(-1e-4, 1e-6, null);
 fail("Should throw NumberIsTooSmallException for negative relative tolerance.");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 try {
 new BrentOptimizer(1e-4, -1e-6, null);
 fail("Should throw NumberIsTooSmallException for negative absolute tolerance.");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }

 try {
 new BrentOptimizer(1e-4, 1e-6, new Maximization());
 } catch (IllegalArgumentException e) {
 fail("Should not throw exception for maximization goal type.");
 }

 try {
 new BrentOptimizer(1e-4, 1e-6, new Minimization());
 } catch (IllegalArgumentException e) {
 fail("Should not throw exception for minimization goal type.");
 }
 }

 @Test
 public void testOptimize() {
 PointValuePair result = optimizer.optimize(objectiveFunction, 1.0, 5.0, null);
 double expected = 2.0;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected * expected, result.getValue(), 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testOptimizeWithSmallRelativeThreshold() {
 optimizer = new BrentOptimizer(1e-12, 1e-6, null);
 optimizer.optimize(objectiveFunction, 1.0, 5.0, null);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testOptimizeWithSmallAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1e-4, 1e-12, null);
 optimizer.optimize(objectiveFunction, 1.0, 5.0, null);
 }

 @Test(expected = MaxEvalException.class)
 public void testOptimizeWithMaxEvalException() {
 objectiveFunction = new MyObjectiveFunctionWithException();
 optimizer.optimize(objectiveFunction, 1.0, 5.0, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testOptimizeWithNegativeInterval() {
 optimizer = new BrentOptimizer(1e-4, 1e-6, null);
 try {
 optimizer.optimize(objectiveFunction, -1.0, 1.0, null);
 fail("Should throw NotStrictlyPositiveException for negative interval lower bound.");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 optimizer.optimize(objectiveFunction, 1.0, -1.0, null);
 fail("Should throw NotStrictlyPositiveException for negative interval upper bound.");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
}

 @Test
 public void testConvergenceChecker() {
 ConvergenceChecker<PointValuePair> myChecker = new MyConvergenceChecker();
 optimizer = new BrentOptimizer(1e-4, 1e-6, myChecker);
 PointValuePair result = optimizer.optimize(objectiveFunction, 1.0, 5.0, null);
 // Check if convergence checker has been called
 assertTrue(myChecker.isConverged());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvergenceCheckerWithNullArgument() {
 optimizer = new BrentOptimizer(1e-4, 1e-6, null);
 try {
 optimizer.setConvergenceChecker(new MyConvergenceChecker());
 fail("Should throw IllegalArgumentException for null argument.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testGoalType() {
 optimizer = new BrentOptimizer(1e-4, 1e-6, new Maximization());
 PointValuePair result = optimizer.optimize(new MyMaxObjectiveFunction(), 1.0, 5.0, null);
 double expected = 4.0;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected * expected, result.getValue(), 1e-6);
 }

 @Test
 public void testOptimizeWithCustomObjectiveFunction() {
 PointValuePair result = optimizer.optimize(new MyCustomObjectiveFunction(), 1.0, 5.0, null);
 double expected = 3.0;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected * expected, result.getValue(), 1e-6);
 }

 @Test
 public void testOptimizeWithCustomObjectiveFunctionAndCustomConvergenceChecker() {
 ConvergenceChecker<PointValuePair> myChecker = new MyCustomConvergenceChecker();
 optimizer = new BrentOptimizer(1e-4, 1e-6, myChecker);
 PointValuePair result = optimizer.optimize(new MyCustomObjectiveFunction(), 1.0, 5.0, null);
 // Check if convergence checker has been called
 assertTrue(myChecker.isConverged());
 }

 @Test
 public void testOptimizeWithSomeFunctionValues() {
 UnivariateObjectiveFunction function = new MySomeFunctionValues();
 PointValuePair result = optimizer.optimize(function, 1.0, 5.0, null);
 double expected = 1.5;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected * expected, result.getValue(), 1e-6);
 }

 @Test
 public void testOptimizeWithCustomStartValue() {
 UnivariateObjectiveFunction function = new MyObjectiveFunction();
 InitialGuess initialGuess = new InitialGuess() {
 @Override
 public double getInitialGuess() {
 return 2.5;
 }
 };
 PointValuePair result = optimizer.optimize(function, 1.0, 5.0, initialGuess);
 double expected = 2.0;
 assertEquals(expected, result.getPoint(), 1e-6);
 assertEquals(expected * expected, result.getValue(), 1e-6);
 }
}

 class MyObjectiveFunction implements UnivariateObjectiveFunction {
 @Override
 public double value(double x) {
 return x * x;
 }

 @Override
 public double derivative(double x) {
 return 2 * x;
 }
}

 class MyObjectiveFunctionWithException extends MyObjectiveFunction {
 @Override
 public double value(double x) {
 if (x > 3.0) {
 throw new MaxEvalException("Maximum number of evaluations reached.");
 }
 return x * x;
 }
}

 class MyCustomObjectiveFunction implements UnivariateObjectiveFunction {
 @Override
 public double value(double x) {
 return Math.sin(x) / x;
 }

 @Override
 public double derivative(double x) {
 return (Math.cos(x) / x - Math.sin(x) / x * x) * (1 / x);
 }
}

 class MyCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 private boolean isConverged = false;

 @Override
 public boolean isConverged(int iteration, PointValuePair previous, PointValuePair current) {
 isConverged = true;
 return true;
 }

 public boolean isConverged() {
 return isConverged;
 }
}

 class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean isConverged(int iteration, PointValuePair previous, PointValuePair current) {
 double delta = Math.abs(current.getPoint() - previous.getPoint());
 return delta < 1e-6 || iteration > 100;
 }
}

 class MySomeFunctionValues implements UnivariateObjectiveFunction {
 @Override
 public double value(double x) {
 double values[] = {1.2, 2.4, 3.6, 1.8};
 return values[(int) Math.floorMod(Math.abs(Math.sin(x)) * 4, 4)] / 2;
 }

 @Override
 public double derivative(double x) {
 return Math.cos(x) / 2;
 }
}