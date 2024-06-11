import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private UnivariateRealFunction function;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        function = mock(UnivariateRealFunction.class);
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
            }
        };
    }

    @Test
    public void testSolveWithDefaults() throws Exception {
        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);

        double result = solver.solve(function, 0.0, 1.0);

        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testSolveWithCustomStartValue() throws Exception {
        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);
        when(function.value(0.5)).thenReturn(0.25);

        double result = solver.solve(function, 0.0, 1.0, 0.5);

        assertEquals(0.5, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(0.0);

        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEval() throws Exception {
        when(function.value(0.0)).thenThrow(new RuntimeException());
        when(function.value(1.0)).thenReturn(0.0);

        solver.solve(function, 0.0, 1.0, 100);
    }

    @Test
    public void testSolveWithRelativeAccuracy() throws Exception {
        when(function.value(0.0)).thenReturn(2.0);
        when(function.value(1.0)).thenReturn(1.0);

        double result = solver.solve(function, 0.0, 1.0, 1e-3);

        assertEquals(0.666666666666667, result, 1e-5);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        when(function.value(0.0)).thenReturn(2.0);
        when(function.value(1.0)).thenReturn(1.0);

        double result = solver.solve(function, 0.0, 1.0, 1e-5);

        assertEquals(0.5, result, 1e-5);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() throws Exception {
        when(function.value(0.0)).thenReturn(2.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(function.value(0.5)).thenReturn(0.5);

        double result = solver.solve(function, 0.0, 1.0, 1e-5, 1e-4);

        assertEquals(0.5, result, 1e-5);
    }

    @Test
    public void testAllowedSolutions() throws Exception {
        when(function.value(0.0)).thenReturn(2.0);
        when(function.value(1.0)).thenReturn(1.0);

        BaseSecantSolver solverLeft = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        double resultLeft = solverLeft.solve(function, 0.0, 1.0);

        assertTrue(resultLeft < 0.5);

        BaseSecantSolver solverRight = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        double resultRight = solverRight.solve(function, 0.0, 1.0);

        assertTrue(resultRight > 0.5);

        BaseSecantSolver solverBelow = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double resultBelow = solverBelow.solve(function, 0.0, 1.0);

        assertTrue(resultBelow < 0.5);

        BaseSecantSolver solverAbove = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        double resultAbove = solverAbove.solve(function, 0.0, 1.0);

        assertTrue(resultAbove > 0.5);
    }
}