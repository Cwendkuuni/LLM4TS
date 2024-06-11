package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

 private static UnivariateRealFunction function1 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4;
 }
 };

 private static UnivariateRealFunction function2 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new UnsupportedOperationException();
 }
 };

 private static BisectionSolver solver = new BisectionSolver();

 @Test
 public void testConstructor() {
 solver = new BisectionSolver();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSolveWithUnsupportedFunction() {
 solver.solve(function2, 0.0, 1.0);
 }

 @Test
 public void testSolveSimpleCase() {
 double root = solver.solve(function1, -2.0, 2.0);
 Assert.assertEquals(0.0, Precision.sub(root, 2.0, 4), 4);
 }

 @Test
 public void testSolveWithInitialValue() {
 double root = solver.solve(function1, -2.0, 2.0, 0.0);
 Assert.assertEquals(0.0, Precision.sub(root, 0.0, 4), 4);
 }

 @Test
 public void testSolveWithOutOfRangeInitialValue() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(function1, 5.0, 3.0, 1.0);
 });
 }

 @Test
 public void testSolveWithMaxIterationReached() {
 Assert.assertThrows(MaxIterationsExceededException.class, () -> {
 solver.solve(function1, -2.0, 2.0, 0.0, 1);
 });
 }

 @Test
 public void testSolveWithZeroWidthInterval() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(function1, 0.0, 0.0);
 });
 }

 @Test
 public void testSolveWithSmallWidthInterval() {
 double root = solver.solve(function1, -2.0, 2.0, 0.001);
 Assert.assertEquals(0.0, Precision.sub(root, 2.0, 4), 4);
 }

 @Test
 public void testSolveWithFunctionValueZeroAtMin() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 0.0;
 }
 }, -1.0, 1.0);
 });
 }

 @Test
 public void testSolveWithFunctionValueZeroAtMax() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 0.0;
 }
 }, -1.0, 1.0);
 });
 }

 @Test
 public void testSolveWithFunctionValueZeroAtMid() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 0.0;
 }
 }, -1.0, 1.0, 0.0);
 });
 }

 @Test
 public void testSolveWithSmallFunctionValueAtMid() {
 double root = solver.solve(function1, -2.0, 2.0, 0.0001);
 Assert.assertEquals(0.0, Precision.sub(root, 2.0, 4), 4);
 }

 @Test
 public void testSolveWithNegativeAbsoluteAccuracy() {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 solver.solve(function1, -2.0, 2.0, -0.001);
 });
 }

 @Test
 public void testSolveWithVerySmallAbsoluteAccuracy() {
 double root = solver.solve(function1, -2.0, 2.0, 1E-12);
 Assert.assertTrue(Math.abs(function1.value(root)) <= 1E-12);
 }

 @Test
 public void testSolveWithVerySmallRelativeAccuracy() {
 double root = solver.solve(function1, -2.0, 2.0, 0.00001);
 Assert.assertTrue(Math.abs(Precision.sub(root, 2.0)) <= 0.00001);
 }
}