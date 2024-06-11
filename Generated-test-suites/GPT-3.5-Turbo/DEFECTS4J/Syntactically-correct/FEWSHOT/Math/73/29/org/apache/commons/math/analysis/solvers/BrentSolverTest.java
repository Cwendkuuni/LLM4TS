package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    
    private BrentSolver brentSolver;

    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
    }

    @Test
    public void testFindRoot() {
        double delta = 1e-10;
        
        // Test case 1
        ComputableFunction function = x -> x;
        ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> {
            if (Math.abs(fLow) < delta) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < delta) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        double x0 = -2.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        boolean result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(0.0, brentSolver.getRoot(), delta);

        // Test case 2
        function = x -> x * x - 1.0;
        x0 = -2.0;
        f0 = function.valueAt(x0);
        x1 = 2.0;
        f1 = function.valueAt(x1);
        result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(-1.0, brentSolver.getRoot(), delta);

        // Test case 3
        function = x -> Math.cos(x) - x;
        x0 = 0.0;
        f0 = function.valueAt(x0);
        x1 = 2.0;
        f1 = function.valueAt(x1);
        result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(0.739085133215, brentSolver.getRoot(), delta);

        // Test case 4
        function = x -> Math.log(x) - 2.0;
        x0 = 2.0;
        f0 = function.valueAt(x0);
        x1 = 4.0;
        f1 = function.valueAt(x1);
        result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(Math.exp(2.0), brentSolver.getRoot(), delta);
        
        // Test case 5 (no root in the interval)
        function = x -> x;
        x0 = 1.0;
        f0 = function.valueAt(x0);
        x1 = 2.0;
        f1 = function.valueAt(x1);
        result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertFalse(result);
    }
}