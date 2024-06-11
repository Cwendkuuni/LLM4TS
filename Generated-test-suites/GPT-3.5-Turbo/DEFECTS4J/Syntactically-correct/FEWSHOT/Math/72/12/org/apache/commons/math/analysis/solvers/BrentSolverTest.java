package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                // TODO: Implement the function being tested
                return 0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // TODO: Implement the convergence checker
                return 0;
            }
        };

        BrentSolver solver = new BrentSolver();

        int maxIter = 100;
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(0.0, solver.getRoot(), epsilon);
    }
}