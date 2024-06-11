package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    private ComputableFunction function;
    private ConvergenceChecker checker;
    private BrentSolver solver;
    
    @Before
    public void setUp() {
        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4.0;
            }
        };
        
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        assertTrue(solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 3.0));
        assertEquals(2.0, solver.getRoot(), 1e-6);
        
        assertTrue(solver.findRoot(function, checker, 100, -1.0, -3.0, 3.0, 3.0));
        assertEquals(-2.0, solver.getRoot(), 1e-6);
        
        assertFalse(solver.findRoot(function, checker, 100, -4.0, -3.0, 3.0, 3.0));
        assertTrue(Double.isNaN(solver.getRoot()));
    }
}