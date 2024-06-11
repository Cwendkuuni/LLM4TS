package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        new BisectionSolver();
    }

    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;

        // Test solver with valid input
        double min = -2.0;
        double max = 2.0;
        double result = solver.solve(function, min, max);
        assertNotNull(result);
        verify(function, times(2)).value(min);
        verify(function, times(2)).value(max);
        assertEquals(expectedResult, result, Precision.SAFE_EPSILON);

        // Test solver with invalid input (min > max)
        min = max;
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, min, max));

        // Test solver with IllegalArgumentException
        function = mock(UnivariateRealFunction.class);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, min, max, 0.0));

        // Test solver with FunctionEvaluationException
        doThrow(new FunctionEvaluationException("Message")).when(function).value(min);
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, min, max));

        // Test solver with MaxIterationsExceededException
        doThrow(new MaxIterationsExceededException("Message")).when(function).value(min);
        solver.setMaximalIterationCount(1);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, min, max));
    }

    @Test
    public void testSolveWithMinAndMax() {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;

        // Test solver with valid input
        double min = -2.0;
        double max = 2.0;
        double result = solver.solve(function, min, max);
        assertNotNull(result);
        verify(function, times(3)).value(min);
        verify(function, times(3)).value(max);
        assertEquals(expectedResult, result, Precision.SAFE_EPSILON);

        // Test solver with invalid input (min > max)
        min = max;
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, min, max));
    }
}