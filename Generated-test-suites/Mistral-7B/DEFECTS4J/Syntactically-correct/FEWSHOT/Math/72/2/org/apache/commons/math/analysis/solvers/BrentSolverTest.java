package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.SinusFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test Cases for BrentSolver class
 */
public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        // Test with a simple function
        ComputableFunction function = new SinusFunction();
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN)).thenReturn(ConvergenceChecker.NEITHER);

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        double f0 = Math.sin(x0);
        double f1 = Math.sin(x1);

        solver.findRoot(function, checker, 10, x0, f0, x1, f1);
        assertTrue(solver.getRoot() - root < 0.001);

        // Test with a function that has multiple roots
        when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN)).thenReturn(ConvergenceChecker.NEITHER);
        double x2 = 3.0;
        double root2 = 1.8;
        double f2 = Math.sin(x2);

        solver.findRoot(function, checker, 10, x0, f0, x1, f1);
        solver.findRoot(function, checker, 10, x1, f1, x2, f2);
        assertArrayEquals(new double[]{root, root2}, new double[]{solver.getRoot(), solver.getRoot()});

        // Test when the function does not converge
        when(checker.converged(Double.NaN, Double.NaN, Double.NaN, Double.NaN)).thenReturn(ConvergenceChecker.NEITHER);
        double x3 = 4.0;
        double root3 = Math.PI;
        double f3 = Math.sin(x3);

        assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
    }

}