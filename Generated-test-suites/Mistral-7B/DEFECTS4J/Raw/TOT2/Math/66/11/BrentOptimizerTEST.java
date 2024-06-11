package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.Value;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBasicOptimization() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 GoalType goalType = GoalType.MINIMIZE;
 double min = -Math.PI;
 double max = Math.PI;
 double startValue = 0.0;
 try {
 PointValuePair result = new BrentOptimizer().optimize(function, goalType, new SimpleBounds(min, max), startValue);
 assertEquals(min, result.getPoint(), 1e-5);
 } catch (MaxIterationsExceededException e) {
 fail("Optimization should have completed in fewer than " + BrentOptimizer.MAX_ITERATION_COUNT + " iterations.");
 }
 }

 @Test
 public void testMaximization() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.cos(x), 2);
 }
 };
 GoalType goalType = GoalType.MAXIMIZE;
 double min = -Math.PI;
 double max = Math.PI;
 double startValue = 0.0;
 try {
 PointValuePair result = new BrentOptimizer().optimize(function, goalType, new SimpleBounds(min, max), startValue);
 assertEquals(max, result.getPoint(), 1e-5);
 } catch (MaxIterationsExceededException e) {
 fail("Optimization should have completed in fewer than " + BrentOptimizer.MAX_ITERATION_COUNT + " iterations.");
 }
 }

 @Test
 public void testMinOutsideBounds() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 GoalType goalType = GoalType.MINIMIZE;
 double min = -2.0;
 double max = 1.0;
 double startValue = 1.5;
 try {
 new BrentOptimizer().optimize(function, goalType, new SimpleBounds(min, max), startValue);
 fail("Optimization should have thrown an exception with bounds out of range.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMaxOutsideBounds() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.cos(x), 2);
 }
 };
 GoalType goalType = GoalType.MAXIMIZE;
 double min = -2.0;
 double max = 1.0;
 double startValue = 1.5;
 try {
 new BrentOptimizer().optimize(function, goalType, new SimpleBounds(min, max), startValue);
 fail("Optimization should have thrown an exception with bounds out of range.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNotStrictlyPositiveAbsoluteAccuracy() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 GoalType goalType = GoalType.MINIMIZE;
 double min = -1.0;
 double max = 1.0;
 double startValue = 0.0;
 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setAbsoluteAccuracy(0.0); // Not strictly positive
 try {
 optimizer.optimize(function, goalType, new SimpleBounds(min, max), startValue);
 fail("Optimization should have thrown an exception with non-positive absolute accuracy.");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testNotStrictlyPositiveMaxIterations() {
 UnivariateRealFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 GoalType goalType = GoalType.MINIMIZE;
 double min = -1.0;
 double max = 1.0;
 double startValue = 0.0;
 BrentOptimizer optimizer = new BrentOptimizer();
 optimizer.setMaxIterations(0); // Not strictly positive
 try {
 optimizer.optimize(function, goalType, new SimpleBounds(min, max), startValue);
 fail("Optimization should have thrown an exception with non-positive maximum iterations.");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }
}