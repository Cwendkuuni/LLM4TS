package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    private BrentSolver solver;

    @Before
    public void setup() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        // Create a dummy function for testing
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        // Test case where root exists in the interval
        boolean found = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, 3, 5);
        assertTrue(found);
        assertEquals(2.0, solver.getRoot(), 1e-9);

        // Test case where root does not exist in the interval
        found = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, 1, -3);
        assertFalse(found);
    }
}