package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    private BrentSolver brentSolver;
    private double epsilon;

    @Before
    public void setup() {
        brentSolver = new BrentSolver();
        epsilon = Math.pow(2.0, -52);
    }

    @Test
    public void testFindRoot() throws FunctionException {
        // Test case 1: Root found at midpoint
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.CONTINUE;
            }
        };
        double x0 = -4;
        double f0 = Math.pow(x0, 2) - 4;
        double x1 = 4;
        double f1 = Math.pow(x1, 2) - 4;
        assertTrue(brentSolver.findRoot(function1, checker1, 10, x0, f0, x1, f1));
        assertEquals(0, brentSolver.getRoot(), epsilon);

        // Test case 2: Root found at one of the bounds
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return x;
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.CONTINUE;
            }
        };
        x0 = -10;
        f0 = x0;
        x1 = 10;
        f1 = x1;
        assertTrue(brentSolver.findRoot(function2, checker2, 10, x0, f0, x1, f1));
        assertEquals(-10, brentSolver.getRoot(), epsilon);
        assertTrue(brentSolver.findRoot(function2, checker2, 10, x1, f1, x0, f0));
        assertEquals(10, brentSolver.getRoot(), epsilon);

        // Test case 3: No root found within maxIter
        ComputableFunction function3 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) + 1;
            }
        };
        ConvergenceChecker checker3 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.CONTINUE;
            }
        };
        x0 = -5;
        f0 = Math.pow(x0, 2) + 1;
        x1 = 5;
        f1 = Math.pow(x1, 2) + 1;
        assertFalse(brentSolver.findRoot(function3, checker3, 10, x0, f0, x1, f1));
        assertTrue(Double.isNaN(brentSolver.getRoot()));
    }
}