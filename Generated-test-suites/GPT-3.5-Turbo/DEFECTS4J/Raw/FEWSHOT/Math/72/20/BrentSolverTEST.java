package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.Identity;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new Identity();
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 100;
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), 1e-15);

        assertFalse(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 1.0));
    }
}