
package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    private static ComputableFunction function = new ComputableFunction() {

        @Override
        public double valueAt(double x) {
            return x * x - 4;
        }
    };

    private static ConvergenceChecker checker = new ConvergenceChecker() {

        @Override
        public int converged(double x1, double f1, double x2, double f2) {
            if (Math.abs(f1) < 1e-6) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(f2) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        
        // Test case 1: Root exists in the interval
        boolean rootFound1;
        try {
            rootFound1 = solver.findRoot(function, checker, 100, 1.0, -3.0, 4.0, 12.0);
        } catch (FunctionException e) {
            rootFound1 = false;
        }
        assertTrue(rootFound1);
        assertEquals(2.0, solver.getRoot(), 1e-6);

        // Test case 2: Root does not exist in the interval
        boolean rootFound2;
        try {
            rootFound2 = solver.findRoot(function, checker, 100, 1.0, -5.0, 4.0, 12.0);
        } catch (FunctionException e) {
            rootFound2 = false;
        }
        assertFalse(rootFound2);
    }
}
