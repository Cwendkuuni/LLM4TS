
package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        solver = new BisectionSolver();
        function = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(-1)).thenReturn(-1.0);
        when(function.value(1)).thenReturn(1.0);
        double result = solver.solve(function, -1, 1);
        assertEquals(0.0, result, 0.0001);

        when(function.value(-10)).thenReturn(-100.0);
        when(function.value(10)).thenReturn(100.0);
        result = solver.solve(function, -10, 10);
        assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(any(Double.class))).thenReturn(1.0);
        solver.solve(function, -1, 1);
        fail("Expected MaxIterationsExceededException");
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(any(Double.class))).thenThrow(new FunctionEvaluationException(0));
        solver.solve(function, -1, 1);
        fail("Expected FunctionEvaluationException");
    }
}
