package org.apache.commons.cli;

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
    private BrentSolver brentSolver;

    @Before
    public void setUp() {
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
        brentSolver = new BrentSolver();
    }

    @Test
    public void testFindRoot_convergedLow() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = 1.0;
        double f1 = -1.0;
        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

        assertTrue(brentSolver.findRoot(function, checker, 10, x0, f0, x1, f1));
        assertEquals(x0, brentSolver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_convergedHigh() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = 1.0;
        double f1 = -1.0;
        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

        assertTrue(brentSolver.findRoot(function, checker, 10, x0, f0, x1, f1));
        assertEquals(x1, brentSolver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_notConverged() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = 1.0;
        double f1 = -1.0;
        when(function.valueAt(x0)).thenReturn(f0);
        when(function.valueAt(x1)).thenReturn(f1);
        when(checker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NOT_CONVERGED);

        assertFalse(brentSolver.findRoot(function, checker, 10, x0, f0, x1, f1));
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_functionException() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        when(function.valueAt(x0)).thenThrow(new FunctionException("Test Function Exception"));

        brentSolver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
    }

}