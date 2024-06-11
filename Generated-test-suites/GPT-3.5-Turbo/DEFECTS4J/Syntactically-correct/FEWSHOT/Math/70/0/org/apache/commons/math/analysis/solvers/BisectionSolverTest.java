package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testSolve_WithValidRange_ReturnsRoot() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = 1.0;
        double max = 2.0;
        double expectedRoot = 1.5;
        
        when(function.value(min)).thenReturn(-1.0);
        when(function.value(max)).thenReturn(1.0);
        
        double result = solver.solve(function, min, max);
        
        assertEquals(expectedRoot, result, 0.0001);
    }

    @Test
    public void testSolve_WithInvalidRange_ThrowsMaxIterationsExceededException() throws FunctionEvaluationException {
        double min = 1.0;
        double max = 1.1;
        
        when(function.value(min)).thenReturn(-1.0);
        when(function.value(max)).thenReturn(1.0);
        
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, min, max));
    }

    @Test
    public void testSolve_WithFunctionEvaluationException_ThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        double min = 1.0;
        double max = 2.0;
        
        when(function.value(min)).thenThrow(new FunctionEvaluationException(min));
        when(function.value(max)).thenReturn(1.0);
        
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, min, max));
    }
}