
package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

    private static final ComputableFunction TEST_FUNCTION = x -> Math.pow(x, 2) - 4;

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        boolean rootFound = solver.findRoot(TEST_FUNCTION, new ConvergenceChecker(), 100,
                1.0, -3.0, 2.0, -2.0);

        assertEquals(true, rootFound);
        assertEquals(2.0, solver.getRoot(), 0.0001);

        rootFound = solver.findRoot(TEST_FUNCTION, new ConvergenceChecker(), 100,
                -3.0, 8.0, -2.0, 4.0);

        assertEquals(true, rootFound);
        assertEquals(-2.0, solver.getRoot(), 0.0001);
    }
}
