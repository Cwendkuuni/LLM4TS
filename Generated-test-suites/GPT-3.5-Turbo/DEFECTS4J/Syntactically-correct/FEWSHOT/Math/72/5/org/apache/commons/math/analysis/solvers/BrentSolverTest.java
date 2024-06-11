package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    @Test
    public void testConstructor() {
        BrentSolver solver = new BrentSolver();
        assertTrue(Double.isNaN(solver.getRoot()));
    }

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        
        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1E-10) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
            }
        };
        double xLow1 = 0;
        double fLow1 = -4;
        double xHigh1 = 3;
        double fHigh1 = 5;
        try {
            assertTrue(solver.findRoot(function1, checker1, 100, xLow1, fLow1, xHigh1, fHigh1));
            assertEquals(2, solver.getRoot(), 1E-10);
        } catch (FunctionException e1) {
            fail(e1.getMessage());
        }
        
        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1E-10) ? ConvergenceChecker.LOW
                        : (Math.abs(fHigh) < 1E-10) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double xLow2 = 0;
        double fLow2 = 0;
        double xHigh2 = 4.5;
        double fHigh2 = -0.995;
        try {
            assertTrue(solver.findRoot(function2, checker2, 100, xLow2, fLow2, xHigh2, fHigh2));
            assertEquals(Math.PI, solver.getRoot(), 1E-10);
        } catch (FunctionException e2) {
            fail(e2.getMessage());
        }
        
        // Test case 3
        ComputableFunction function3 = new ComputableFunction() {
            
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.exp(x) - 1;
            }
        };
        ConvergenceChecker checker3 = new ConvergenceChecker() {
            
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fHigh) < 1E-10) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double xLow3 = -2;
        double fLow3 = -1;
        double xHigh3 = 0;
        double fHigh3 = -1;
        try {
            assertTrue(solver.findRoot(function3, checker3, 100, xLow3, fLow3, xHigh3, fHigh3));
            assertEquals(0, solver.getRoot(), 1E-10);
        } catch (FunctionException e3) {
            fail(e3.getMessage());
        }
        
        // Test case 4
        ComputableFunction function4 = new ComputableFunction() {
            
            @Override
            public double valueAt(double x) throws FunctionException {
                return x;
            }
        };
        ConvergenceChecker checker4 = new ConvergenceChecker() {
            
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.NONE;
            }
        };
        double xLow4 = -1;
        double fLow4 = -1;
        double xHigh4 = 1;
        double fHigh4 = 1;
        try {
            assertFalse(solver.findRoot(function4, checker4, 100, xLow4, fLow4, xHigh4, fHigh4));
        } catch (FunctionException e4) {
            fail(e4.getMessage());
        }
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.root = 3.5; // Set the root to a specific value for testing
        assertEquals(3.5, solver.getRoot(), 0.0);
    }

}