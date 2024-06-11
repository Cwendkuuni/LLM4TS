
package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.Constant;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: Root between lower and upper bound
        double x0 = -1.0;
        double f0 = new Constant(-1.0).value();
        double x1 = 1.0;
        double f1 = new Constant(1.0).value();
        ComputableFunction function = new Constant(0.0);
        ConvergenceChecker checker = new NewtonRaphsonSolver();

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), 1e-6);

        // Test case 2: Root at lower bound
        x0 = 0.0;
        f0 = new Constant(0.0).value();
        x1 = 1.0;
        f1 = new Constant(1.0).value();
        function = new Constant(0.0);
        checker = new NewtonRaphsonSolver();

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), 1e-6);

        // Test case 3: Root at upper bound
        x0 = -1.0;
        f0 = new Constant(-1.0).value();
        x1 = 0.0;
        f1 = new Constant(0.0).value();
        function = new Constant(0.0);
        checker = new NewtonRaphsonSolver();

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), 1e-6);

        // Test case 4: No root
        x0 = -1.0;
        f0 = new Constant(-1.0).value();
        x1 = 1.0;
        f1 = new Constant(1.0).value();
        function = new Constant(2.0);
        checker = new NewtonRaphsonSolver();

        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }
}
