package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRootSuccess() throws FunctionException {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(function.valueAt(2.0)).thenReturn(0.5);

        when(checker.converged(0.0, 0.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.NONE);

        BrentSolver brentSolver = new BrentSolver();

        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0);

        assertTrue(result);
        assertEquals(0.5, brentSolver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootFail() throws FunctionException {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(function.valueAt(2.0)).thenReturn(0.5);

        when(checker.converged(0.0, 0.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.NONE);
        when(checker.converged(0.0, 0.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.LOW);

        BrentSolver brentSolver = new BrentSolver();

        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 0.0, 1.0, 1.0);

        assertFalse(result);
        assertEquals(Double.NaN, brentSolver.getRoot());
    }

    @Test
    public void testFindRootFailMaxIter() throws FunctionException {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(0.0);
        when(function.valueAt(1.0)).thenReturn(1.0);
        when(function.valueAt(2.0)).thenReturn(0.5);

        when(checker.converged(0.0, 0.0, 1.0, 0.5)).thenReturn(ConvergenceChecker.NONE);

        BrentSolver brentSolver = new BrentSolver();

        boolean result = brentSolver.findRoot(function, checker, 0, 0.0, 0.0, 1.0, 1.0);

        assertFalse(result);
        assertEquals(Double.NaN, brentSolver.getRoot());
    }
}