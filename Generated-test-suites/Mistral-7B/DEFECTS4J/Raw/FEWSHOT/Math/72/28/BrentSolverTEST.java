package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(-1.0);

        BrentSolver brentSolver = new BrentSolver();
        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0);
        Assert.assertTrue(result);
        Assert.assertEquals(0.0, brentSolver.getRoot(), 0.01);
    }

    @Test
    public void testFindRootWithConvergence() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(1.0);

        when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);

        BrentSolver brentSolver = new BrentSolver();
        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0);
        Assert.assertTrue(result);
        Assert.assertEquals(0.0, brentSolver.getRoot(), 0.01);
    }

    @Test
    public void testFindRootWithDivergence() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(1.0);

        when(checker.converged(0.0, 1.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);

        BrentSolver brentSolver = new BrentSolver();
        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 1.0);
        Assert.assertFalse(result);
        Assert.assertEquals(Double.NaN, brentSolver.getRoot());
    }

    @Test
    public void testFindRootWithNoConvergence() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(-1.0);

        when(checker.converged(0.0, 1.0, 1.0, -1.0)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

        BrentSolver brentSolver = new BrentSolver();
        boolean result = brentSolver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, -1.0);
        Assert.assertFalse(result);
        Assert.assertEquals(Double.NaN, brentSolver.getRoot());
    }
}