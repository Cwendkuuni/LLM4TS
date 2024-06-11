package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 double[] result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 0.001);
 assertNull(result);
 }

 @Test
 public void testSolveWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 double result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
 assertEquals(0.0, result, 0.001);
 }

 @Test
 public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 double result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 0.0001);
 assertEquals(0.0, result, 0.0001);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
 when(solver.solve(function, 1.0, 2.0)).thenReturn(Double.NaN);
 when(LazyHolder.FACTORY.newDefaultSolver()).thenReturn(solver);

 double[] result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 0.001, 0);
 assertNull(result);
 }

 @Test
 public void testBracket() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0);
 assertArrayEquals(new double[]{1.0, 2.0}, result);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 UnivariateRealSolver solver = mock(UnivariateRealSolver.class);
 when(solver.solve(function, 1.0, 2.0)).thenReturn(Double.NaN);
 when(LazyHolder.FACTORY.newDefaultSolver()).thenReturn(solver);

 double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 0);
 assertNull(result);
 }

 @Test
 public void testBracketWithNegativeFunctionValues() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(-1.0)).thenReturn(-1.0);
 when(function.value(-0.5)).thenReturn(0.5);
 when(function.value(0.0)).thenReturn(0.0);
 when(function.value(0.5)).thenReturn(1.0);
 when(function.value(1.0)).thenReturn(1.0);

 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 2.0);
 assertArrayEquals(new double[]{0.5, 1.0}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealSolverUtils.bracket(null, 1.0, 2.0, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithNegativeInitial() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 UnivariateRealSolverUtils.bracket(function, -1.0, 1.0, 2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithInitialGreaterThanUpperBound() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 UnivariateRealSolverUtils.bracket(function, 1.0, 0.0, 2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketWithLowerBoundGreaterThanUpperBound() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = mock(UnivariateRealFunction.class);
 when(function.value(Double.NaN)).thenThrow(new FunctionEvaluationException());
 when(function.value(0.0)).thenReturn(0.0);

 UnivariateRealSolverUtils.bracket(function, 1.0, 2.0, 1.0);
 }
}