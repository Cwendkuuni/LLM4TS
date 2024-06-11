import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    private BrentSolver solver;
    private ComputableFunction function;
    private ConvergenceChecker checker;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
    }

    @Test
    public void testFindRootWithRootInInterval() throws FunctionException {
        double x0 = -1;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);
        int maxIter = 100;

        // Mock convergence checker to return LOW for first iteration
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

        boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(x0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootWithRootOnBoundary() throws FunctionException {
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);
        int maxIter = 100;

        // Mock convergence checker to return HIGH for first iteration
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

        boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(x1, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootWithNoRootInInterval() throws FunctionException {
        double x0 = -1;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);
        int maxIter = 100;

        // Mock convergence checker to return neither LOW nor HIGH for first iteration
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NOTHING);

        boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertFalse(foundRoot);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithFunctionException() throws FunctionException {
        double x0 = -1;
        double f0 = Double.NaN;
        double x1 = 1;
        double f1 = Double.NaN;
        int maxIter = 100;

        solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }
}