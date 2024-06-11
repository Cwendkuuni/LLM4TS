package org.apache.commons.cli;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double x0 = 0.0;
 double x1 = 1.5708; // PI/2
 double result = UnivariateRealSolverUtils.solve(f, x0, x1);
 assertEquals(Math.PI / 4, result, 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("Evaluation error");
 }
 };
 UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 UnivariateRealFunction brokenF = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return f.value(x) * 2; // Intentionally broken
 }
 };
 UnivariateRealSolverUtils.solve(brokenF, 0.0, 1.5708);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = null;
 assertNull(UnivariateRealSolverUtils.solve(f, 0.0, 1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 assertNull(UnivariateRealSolverUtils.solve(f, 1.5708, 0.0)); // Reversed interval
 }

 @Test
 public void testSolveWithDefaultSolverAndAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double x0 = 0.0;
 double x1 = 1.5708; // PI/2
 double absoluteAccuracy = 0.001;
 double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 assertEquals(Math.PI / 4, result, absoluteAccuracy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithInvalidAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double x0 = 0.0;
 double x1 = 1.5708; // PI/2
 double absoluteAccuracy = -0.001;
 UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
 }

 @Test
 public void testBracket() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double initial = 0.0;
 double lowerBound = 0.0;
 double upperBound = 1.5708; // PI/2
 double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 assertArrayEquals(new double[]{initial - 0.01, initial + 0.01}, bracketedValues, 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double initial = 0.0;
 double lowerBound = 0.0;
 double upperBound = 1.5708; // PI/2
 UnivariateRealFunction brokenF = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return f.value(x) * 2; // Intentionally broken
 }
 };
 double[] bracketedValues = UnivariateRealSolverUtils.bracket(brokenF, initial, lowerBound, upperBound);
 assertNull(bracketedValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = null;
 double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 1.0);
 assertNull(bracketedValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double initial = 0.0;
 double lowerBound = 1.5708; // PI/2
 double upperBound = 0.0;
 double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
 assertNull(bracketedValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInvalidMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 double initial = 0.0;
 double lowerBound = 0.0;
 double upperBound = 1.5708; // PI/2
 int maximumIterations = -1;
 double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
 assertNull(bracketedValues);
 }

 @Test
 public void testMidpoint() {
 double a = 1.5;
 double b = 3.5;
 assertEquals(2.5, UnivariateRealSolverUtils.midpoint(a, b), 0.001);
 }

}