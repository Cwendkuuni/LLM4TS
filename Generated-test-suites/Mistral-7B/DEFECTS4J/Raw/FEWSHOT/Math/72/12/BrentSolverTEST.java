package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.mockito.Mockito.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-2.0);
        when(checker.converged(-1)).thenReturn(ConvergenceChecker.LOW);

        brentSolver.findRoot(function, checker, 10, x0, 1.0, x1, -2.0);
        Assert.assertEquals(x0, brentSolver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootWithConvergedHigh() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-2.0);
        when(checker.converged(1)).thenReturn(ConvergenceChecker.HIGH);

        try {
            brentSolver.findRoot(function, checker, 10, x0, 1.0, x1, -2.0);
            fail("FunctionException expected");
        } catch (FunctionException e) {
            // expected
        }
    }

    @Test
    public void testFindRootWithConvergedSameValue() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenReturn(0.0);
        when(function.valueAt(x1)).thenReturn(0.0);
        when(checker.converged(0)).thenReturn(ConvergenceChecker.CONVERGED);

        brentSolver.findRoot(function, checker, 10, x0, 0.0, x1, 0.0);
        Assert.assertEquals(x0, brentSolver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootWithConvergedZero() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenReturn(0.0);
        when(function.valueAt(x1)).thenReturn(0.0);
        when(checker.converged(0)).thenReturn(ConvergenceChecker.CONVERGED_ZERO);

        brentSolver.findRoot(function, checker, 10, x0, 0.0, x1, 0.0);
        Assert.assertEquals(x0, brentSolver.getRoot(), 0.0);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenThrow(new RuntimeException());

        brentSolver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
    }

    @Test
    public void testFindRootWithMaxIterations() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function.valueAt(x0)).thenReturn(1.0);
        when(function.valueAt(x1)).thenReturn(-2.0);
        when(checker.converged(-1)).thenReturn(ConvergenceChecker.LOW);

        brentSolver.findRoot(function, checker, 0, x0, 1.0, x1, -2.0);
        Assert.assertFalse(brentSolver.findRoot(function, checker, 0, x0, 1.0, x1, -2.0));
    }

    @Test
    public void testFindRootWithDifferentFunctions() {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function1 = mock(ComputableFunction.class);
        ComputableFunction function2 = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);

        BrentSolver brentSolver = new BrentSolver();

        when(function1.valueAt(x0)).thenReturn(1.0);
        when(function1.valueAt(x1)).thenReturn(-2.0);
        when(function2.valueAt(x0)).thenReturn(1.0);
        when(function2.valueAt(x1)).thenReturn(-2.0);
        when(checker.converged(-1)).thenReturn(ConvergenceChecker.LOW);

        brentSolver.findRoot(function1, checker, 10, x0, 1.0, x1, -2.0);
        double root = brentSolver.getRoot();
        brentSolver.findRoot(function2, checker, 10, x0, 1.0, x1, -2.0);
        Assert.assertEquals(root, brentSolver.getRoot());
    }

}