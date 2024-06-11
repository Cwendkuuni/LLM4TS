package org.apache.commons.cli.roots; // assuming you have created a new package name "roots" under org.apache.commons.cli

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_convergedAtLowBound() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.LOW);

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Double.isNaN(solver.getRoot()));

        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.LOW);
        when(function.valueAt(1.0)).thenReturn(0.0);

        solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Math.abs(solver.getRoot() - 1.0) < 1e-8);
    }

    @Test
    public void testFindRoot_convergedAtHighBound() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.HIGH);

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Double.isNaN(solver.getRoot()));

        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.HIGH);
        when(function.valueAt(1.0)).thenReturn(0.0);

        solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Math.abs(solver.getRoot() - 1.0) < 1e-8);
    }

    @Test
    public void testFindRoot_convergedAtRoot() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(-1.0);
        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.UNDEFINED);

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Double.isNaN(solver.getRoot()));

        when(checker.converged(0.5, 0.5, 0.5, 0.5)).thenReturn(ConvergenceChecker.LOW);
        when(function.valueAt(0.5)).thenReturn(0.0);

        solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Math.abs(solver.getRoot() - 0.5) < 1e-8);
    }

    @Test
    public void testFindRoot_notConverged() {
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(0.0)).thenReturn(1.0);
        when(function.valueAt(1.0)).thenReturn(-1.0);
        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.UNDEFINED);

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Double.isNaN(solver.getRoot()));

        when(checker.converged(0.0, 1.0, 1.0, 0.0)).thenReturn(ConvergenceChecker.UNDEFINED);
        when(function.valueAt(1.0)).thenReturn(1.0);

        solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 0.0, 1.0, 1.0, 0.0));
        assertTrue(Double.isNaN(solver.getRoot()));
    }
}