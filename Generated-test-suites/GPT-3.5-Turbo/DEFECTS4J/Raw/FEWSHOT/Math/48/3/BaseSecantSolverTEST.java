import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        f = mock(UnivariateRealFunction.class);
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) throws ConvergenceException {
                return f.value(point);
            }
        };
    }

    @Test
    public void testSolve_ExactRootSolution() throws ConvergenceException {
        double result = 0.0;

        when(f.value(0.0)).thenReturn(0.0);
        result = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertEquals(0.0, result, 1e-6);

        when(f.value(1.0)).thenReturn(0.0);
        result = solver.solve(100, f, 0.0, 2.0, 1.0);
        assertEquals(1.0, result, 1e-6);

        when(f.value(2.0)).thenReturn(0.0);
        result = solver.solve(100, f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_ApproximateRootSolution() throws ConvergenceException {
        double result = 0.0;

        when(f.value(0.0)).thenReturn(1.0);
        when(f.value(1.0)).thenReturn(-1.0);
        result = solver.solve(100, f, 0.0, 2.0, 0.0);
        assertEquals(0.5, result, 1e-6);

        when(f.value(1.0)).thenReturn(2.0);
        when(f.value(2.0)).thenReturn(-2.0);
        result = solver.solve(100, f, 1.0, 3.0, 1.0);
        assertEquals(1.5, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() throws ConvergenceException {
        when(f.value(1.0)).thenReturn(1.0);
        when(f.value(2.0)).thenReturn(1.0);
        solver.solve(100, f, 1.0, 3.0, 1.0);
    }

}