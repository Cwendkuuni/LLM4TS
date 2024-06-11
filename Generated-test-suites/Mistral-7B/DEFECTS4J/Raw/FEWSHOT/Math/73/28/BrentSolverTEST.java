package org.apache.commons.cli;

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

    private ComputableFunction function = mock(ComputableFunction.class);
    private ConvergenceChecker checker = mock(ConvergenceChecker.class);

    @Test
    public void testFindRoot_ConvergedAtLowerBound() throws FunctionException {
        when(function.valueAt(0.0)).thenReturn(0.0);

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 0.0;

        assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_ConvergedAtUpperBound() throws FunctionException {
        when(function.valueAt(1.0)).thenReturn(0.0);

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 1.0;

        assertTrue(solver.findRoot(function, checker, 10, x0, 1.0, x1, 0.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_ConvergedAtRoot() throws FunctionException {
        when(function.valueAt(0.5)).thenReturn(0.0);

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 0.5;

        assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_ConvergedAtLimit() throws FunctionException {
        when(function.valueAt(0.5)).thenReturn(Math.pow(-1, new java.util.Random().nextBoolean()) * epsilon);

        BrentSolver solver = new BrentSolver();
        double x0 = 0.5 - 1.0;
        double x1 = 0.5 + 1.0;
        double root = 0.5;

        assertTrue(solver.findRoot(function, checker, 10, x0, Math.pow(-1, new java.util.Random().nextBoolean()) * epsilon, x1, Math.pow(-1, new java.util.Random().nextBoolean()) * epsilon));
        assertEquals(root, solver.getRoot(), 2 * epsilon);
    }

    @Test
    public void testFindRoot_NotConverged() {
        when(function.valueAt(0.5)).thenThrow(new FunctionException("Test function exception"));

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 0.5;

        assertFalse(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertNull(solver.getRoot());
    }

    @Test
    public void testFindRoot_Checkers() {
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.LOW);
        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.HIGH);
        when(checker.converged(0.0, 0.0, 1.0, 1.0)).thenReturn(ConvergenceChecker.NONE);

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 0.5;

        // Converged at lower bound
        when(checker.converged(0.0, 0.0, 0.5, 0.0)).thenReturn(ConvergenceChecker.LOW);
        assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertEquals(root, solver.getRoot(), epsilon);

        // Converged at upper bound
        when(checker.converged(0.0, 0.0, 0.5, 0.0)).thenReturn(ConvergenceChecker.HIGH);
        assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertEquals(root, solver.getRoot(), epsilon);

        // Not converged
        when(checker.converged(0.0, 0.0, 0.5, 0.0)).thenReturn(ConvergenceChecker.NONE);
        assertFalse(solver.findRoot(function, checker, 10, x0, 0.0, x1, 1.0));
        assertNull(solver.getRoot());
    }
}