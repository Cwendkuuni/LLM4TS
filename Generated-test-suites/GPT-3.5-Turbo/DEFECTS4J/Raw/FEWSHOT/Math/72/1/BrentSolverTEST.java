package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
    
    private BrentSolver solver;
    
    @Before
    public void setUp() {
        solver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot() throws FunctionException {
        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x - 3;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
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
        boolean result1 = solver.findRoot(function1, checker1, 100, 2, -1, 4, 1);
        assertTrue(result1);
        assertEquals(3, solver.getRoot(), 1e-6);
        
        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 2;
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
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
        boolean result2 = solver.findRoot(function2, checker2, 100, 1, -1, 2, 2);
        assertTrue(result2);
        assertEquals(Math.sqrt(2), solver.getRoot(), 1e-6);
    }
}