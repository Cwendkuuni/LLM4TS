package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FuncTestUtils;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_Convergence() throws FunctionException {
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        ComputableFunction function = FuncTestUtils.sinX();
        double x0 = 0.0;
        double x1 = 2.0;
        double root = 1.274316662; // Approximate root

        when(checker.converged(x0, function.valueAt(x0), x1, function.valueAt(x1)))
                .thenReturn(ConvergenceChecker.LOW);
        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_Divergence() throws FunctionException {
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        ComputableFunction function = FuncTestUtils.sinX();
        double x0 = 0.0;
        double x1 = 2.0;
        double root = 1.274316662; // Approximate root

        when(checker.converged(x0, function.valueAt(x0), x1, function.valueAt(x1)))
                .thenReturn(ConvergenceChecker.HIGH);
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertEquals(Double.NaN, solver.getRoot());
    }

    @Test
    public void testFindRoot_ConvergenceCheckerException() throws FunctionException {
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        ComputableFunction function = FuncTestUtils.sinX();
        double x0 = 0.0;
        double x1 = 2.0;
        double root = 1.274316662; // Approximate root

        when(checker.converged(x0, function.valueAt(x0), x1, function.valueAt(x1)))
                .thenThrow(new RuntimeException("Convergence checker exception"));
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertEquals(Double.NaN, solver.getRoot());
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionException() throws FunctionException {
        ComputableFunction function = () -> {throw new FunctionException("Function exception");};
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, null, 100, 0.0, 0.0, 0.0, 0.0);
    }
}