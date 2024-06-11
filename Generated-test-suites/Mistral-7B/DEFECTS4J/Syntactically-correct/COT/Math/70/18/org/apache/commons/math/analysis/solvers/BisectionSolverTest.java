package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BisectionSolverTest {

 private UnivariateRealFunction f;
 private BisectionSolver solver;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 solver = new BisectionSolver();
 }

 @Test
 public void testEmptyConstructor() {
 solver = new BisectionSolver();
 Assert.assertEquals(100, solver.getMaxIterations());
 Assert.assertEquals(1.0E-6, solver.getRelativeAccuracy(), 0.0);
 }

 @Test
 public void testConstructorWithFunction() {
 solver = new BisectionSolver(f);
 Assert.assertNull(solver.getMin());
 Assert.assertNull(solver.getMax());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSolveWithMinAndMax() {
 solver.solve(f, Double.NaN, Double.NaN);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSolveWithInitialValue() {
 solver.solve(f, Double.NaN, Double.NaN, Double.NaN);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionEvaluationException() {
 f = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws FunctionEvaluationException {
 throw new FunctionEvaluationException("Function evaluation error.");
 }
 };
 solver.solve(f, 0.0, 1.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveWithMaxIterationsExceededException() {
 f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 solver.setMaxIterations(0);
 solver.solve(f, 0.0, 1.0);
 }

 @Test
 public void testSolveSimpleExample() {
 double root = solver.solve(f, -1.5, 1.5);
 Assert.assertNotEquals(Double.NaN, root, 0.0);
 Assert.assertEquals(0.0, Precision.abs(Math.pow(root, 2) - 1.0), 1.0E-6);
 }

 @Test
 public void testSolveNearRoot() {
 double root = solver.solve(f, 0.5, 0.6);
 Assert.assertNotEquals(Double.NaN, root, 0.0);
 Assert.assertEquals(0.0, Precision.abs(Math.pow(root, 2) - 1.0), 1.0E-6);
 }

 @Test
 public void testSolveWithInitialValueCloseToRoot() {
 double root = solver.solve(f, 0.99, 1.01);
 Assert.assertNotEquals(Double.NaN, root, 0.0);
 Assert.assertEquals(0.0, Precision.abs(Math.pow(root, 2) - 1.0), 1.0E-6);
 }

 @Test
 public void testSolveWithInitialValueFarFromRoot() {
 double root = solver.solve(f, -5.0, 5.0);
 Assert.assertNotEquals(Double.NaN, root, 0.0);
 Assert.assertEquals(0.0, Precision.abs(Math.pow(root, 2) - 1.0), 1.0E-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSolveWithSmallAbsoluteAccuracy() {
 solver.setRelativeAccuracy(0.0);
 solver.setAbsoluteAccuracy(1.0E-12);
 solver.solve(f, 0.0, 1.0);
 }

 @Test
 public void testSolveWithLargeMaxIterations() {
 solver.setMaxIterations(1000000);
 solver.solve(f, 0.0, 1.0);
 }
}