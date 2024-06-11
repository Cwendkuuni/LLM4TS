package org.apache.commons.math3.optimization.general.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.GoalType;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearEqualityConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearInequalityConstraint;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearObjectiveFunctionGrad;

public class GaussNewtonOptimizerTest {

 private RealMatrix weightMatrix;
 private double[] targetValues;
 private RealVector startPoint;

 @org.junit.Before
 public void setUp() {
 weightMatrix = new BlockRealMatrix(new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 targetValues = new double[]{1.0, 1.0};
 startPoint = new ArrayRealVector(new double[]{0.0, 0.0}, false);
 }

 @org.junit.Test
 public void testGaussNewtonOptimizer_DefaultConstructor() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(true, optimizer.isUsingLuDecomposition());
 assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
 }

 @org.junit.Test
 public void testGaussNewtonOptimizer_ConstructorWithConvergenceChecker() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(true, optimizer.isUsingLuDecomposition());
 assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
 }

 @org.junit.Test
 public void testGaussNewtonOptimizer_ConstructorWithUseLUAndConvergenceChecker() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
 assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals(true, optimizer.isUsingLuDecomposition());
 assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
 }

 @org.junit.Test
 public void testDoOptimize_WithValidInputAndDefaultSettings() throws OptimizationException {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 assertEquals(targetValues.length, optimizer.getNumberOfVariables());
 PointValuePair result = optimizer.optimize(new LinearObjectiveFunction(targetValues, weightMatrix), startPoint);
 assertEquals(targetValues, result.getPoint(), 1e-10);
 }

 @org.junit.Test
 public void testDoOptimize_WithValidInputAndSpecifiedSettings() throws OptimizationException {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
 assertEquals(targetValues.length, optimizer.getNumberOfVariables());
 PointValuePair result = optimizer.optimize(new LinearObjectiveFunction(targetValues, weightMatrix), startPoint);
 assertEquals(targetValues, result.getPoint(), 1e-10);
 }

 @org.junit.Test
 public void testDoOptimize_WithValidInput_LUDecompositionFails() throws OptimizationException, SingularMatrixException {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
 // Create a problem where LUDecomposition throws an exception
 RealMatrix weightedJacobian = new BlockRealMatrix(new double[][]{
 {9.0, 1.0},
 {1.0, 9.0}
 });
 LinearObjectiveFunction obj = new LinearObjectiveFunction(targetValues, weightMatrix);
 LinearObjectiveFunctionGrad jac = obj.valueAndGradient(startPoint);
 PointVectorValuePair previous = new PointVectorValuePair(new ArrayRealVector(startPoint), obj.value(startPoint));
 PointVectorValuePair current = new PointVectorValuePair(new ArrayRealVector(new double[]{-1.0, -1.0}), new double[]{1.0, 1.0});
 RealMatrix mA = new BlockRealMatrix(new double[][]{
 {10.0, 1.0},
 {1.0, 10.0}
 });
 RealVector b = new ArrayRealVector(new double[]{1.0, 1.0}, false);
 RealMatrix a = new BlockRealMatrix(new double[][]{
 {10.0, 1.0},
 {1.0, 10.0}
 });
 try {
 optimizer.doOptimize();
 fail("Expected ConvergenceException to be thrown");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testDoOptimize_WithInvalidInput_NullArgumentException() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, startPoint));
 assertThrows(NullArgumentException.class, () -> optimizer.optimize(new LinearObjectiveFunction(targetValues, null), startPoint));
 }

 @org.junit.Test
 public void testDoOptimize_WithInvalidInput_SingularMatrixException() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 LinearObjectiveFunction obj = new LinearObjectiveFunction(targetValues, weightMatrix);
 LinearObjectiveFunctionGrad jac = obj.valueAndGradient(startPoint);
 PointVectorValuePair previous = new PointVectorValuePair(new ArrayRealVector(startPoint), obj.value(startPoint));
 PointVectorValuePair current = new PointVectorValuePair(new ArrayRealVector(new double[]{-1.0, -1.0}), new double[]{1.0, 1.0});
 RealMatrix mA = new BlockRealMatrix(new double[][]{
 {1.0, 1.0},
 {1.0, 1.0}
 });
 RealVector b = new ArrayRealVector(new double[]{1.0, 1.0}, false);
 try {
 optimizer.doOptimize();
 fail("Expected SingularMatrixException to be thrown");
 } catch (SingularMatrixException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testDoOptimize_WithValidInputAndLinearConstraints() throws OptimizationException {
 LinearConstraintSet constraints = new LinearConstraintSet();
 constraints.addConstraint(new LinearEqualityConstraint(1, 0, 1.0));
 constraints.addConstraint(new LinearInequalityConstraint(1, 1, 2.0));
 LinearObjectiveFunction obj = new LinearObjectiveFunction(targetValues, weightMatrix);
 PointVectorValuePair previous = new PointVectorValuePair(new ArrayRealVector(startPoint), obj.value(startPoint));
 PointVectorValuePair current = new PointVectorValuePair(new ArrayRealVector(new double[]{-0.5, -0.5}), new double[]{1.0, 1.0});
 RealMatrix mA = new BlockRealMatrix(new double[][]{
 {1.1, 1.0},
 {1.0, 1.1}
 });
 RealVector b = new ArrayRealVector(new double[]{0.5, 0.5}, false);
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
 optimizer.optimize(obj, startPoint, constraints);
 assertEquals(targetValues, current.getPoint(), 1e-10);
 }

 @org.junit.Test
 public void testDoOptimize_WithValidInputAndNonlinearConstraints() {
 // This test case is not possible since GaussNewtonOptimizer does not support nonlinear constraints
 }
}