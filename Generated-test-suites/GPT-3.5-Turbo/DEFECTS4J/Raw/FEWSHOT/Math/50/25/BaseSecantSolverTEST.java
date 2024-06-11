import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = mock(UnivariateRealFunction.class);
    }

    @After
    public void tearDown() {
        function = null;
        solver = null;
    }

    @Test
    public void testSolve_Bracketing() {
        solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return function.value(point);
            }
        };

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(1.0);

        double result = solver.solve(100, function, 0.0, 1.0);

        verify(function, times(2)).value(anyDouble());
        assertEquals(0.0, result, 1e-8);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InternalError() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois) {
            @Override
            protected double computeObjectiveValue(double point) {
                return Double.NaN;
            }
        };

        solver.solve(100, function, 0.0, 1.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InternalError2() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois) {
            @Override
            protected double computeObjectiveValue(double point) {
                throw new RuntimeException("Internal error");
            }
        };

        solver.solve(100, function, 0.0, 1.0);
    }

    @Test
    public void testSolve_MaximalEvaluationCountExceeded() {
        solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return function.value(point);
            }
        };

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(1.0);

        double result = solver.solve(2, function, 0.0, 1.0);

        verify(function, times(2)).value(anyDouble());
        assertEquals(0.0, result, 1e-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_NoBracketing() {
        solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return function.value(point);
            }
        };

        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(1.0);

        solver.solve(100, function, 0.0, 1.0);
    }

    @Test
    public void testSolve_SolutionNotFound() {
        solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return function.value(point);
            }
        };

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(-1.0);

        double result = solver.solve(100, function, 0.0, 1.0);

        verify(function, times(2)).value(anyDouble());
        assertEquals(0.0, result, 1e-8);
    }

    @Test
    public void testSolve_ZeroAccuracy() {
        solver = new BaseSecantSolver(0, 0, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return function.value(point);
            }
        };

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(1.0)).thenReturn(1.0);

        double result = solver.solve(100, function, 0.0, 1.0);

        verify(function, times(1)).value(anyDouble());
        assertEquals(0.5, result, 1e-8);
    }
}