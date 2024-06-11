package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        BrentSolver solver = new BrentSolver();

        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.DEFAULT);
        when(function.valueAt(0.5)).thenReturn(0.5);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(0.5, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        BrentSolver solver = new BrentSolver();

        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.DEFAULT);
        when(function.valueAt(0.5)).thenReturn(-0.5);

        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.root = 1.0;

        assertEquals(1.0, solver.getRoot(), 1e-6);
    }

}