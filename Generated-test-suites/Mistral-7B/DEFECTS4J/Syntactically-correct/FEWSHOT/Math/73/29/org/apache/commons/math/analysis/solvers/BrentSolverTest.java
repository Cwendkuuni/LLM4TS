package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
    private ComputableFunction function;
    private ConvergenceChecker checker;
    private BrentSolver solver;

    @Before
    public void setUp() {
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot_converged_low() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);

        assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
        assertEquals(0.0, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRoot_converged_high() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);

        assertTrue(solver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0));
        assertEquals(1.0, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRoot_converged_root() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.ROOT);

        assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0));
        assertEquals(1.0, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRoot_notConverged() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(0.0);
        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_functionException() throws FunctionException {
        when(function.valueAt(0.0)).thenThrow(new FunctionException());

        solver.findRoot(function, checker, 10, 0.0, 0.0, 0.0, 0.0);
    }

    @Test
    public void testFindRoot_maxIterationsReached() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);

        assertFalse(solver.findRoot(function, checker, 0, 0.0, 0.0, 1.0, 1.0));
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }
}