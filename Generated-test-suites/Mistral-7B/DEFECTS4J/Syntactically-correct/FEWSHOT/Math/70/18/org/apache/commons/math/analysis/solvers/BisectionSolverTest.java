package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolverImpl;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        // Test default constructor
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);

        // Test constructor with function
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BisectionSolver solverWithFunction = new BisectionSolver(function);
        assertNotNull(solverWithFunction);
    }

    @Test
    public void testSolveWithValidInput() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(-1.0);

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testSolveWithMinAndMaxCloseToZero() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenReturn(0.1);
        when(function.value(1.0)).thenReturn(-0.1);

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0.0, 1.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMinGreaterThanMax() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(-1.0);

        BisectionSolver solver = new BisectionSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, 1.0, 0.0));
    }

    @Test
    public void testSolveWithFunctionThrowsException() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenThrow(new FunctionEvaluationException("Error evaluating function at 0.0"));

        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, 0.0, 1.0));
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenReturn(0.1);
        when(function.value(1.0)).thenReturn(-0.1);

        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, 0.0, 1.0, 0));
    }

    @Test
    public void testMidpoint() {
        double min = 0.0;
        double max = 1.0;
        double expected = 0.5;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1e-6);
    }
}