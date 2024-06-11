package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // define your function here
                return 0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // define your convergence criteria here
                return 0;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, 0, 0, 1, 1);
        assertTrue(result);
        double root = solver.getRoot();
        // assert the expected root value here
    }
}