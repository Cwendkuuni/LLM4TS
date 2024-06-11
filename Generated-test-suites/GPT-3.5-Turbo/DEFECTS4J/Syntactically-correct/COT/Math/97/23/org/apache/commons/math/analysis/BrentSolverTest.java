
import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
  
    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                // Define the function here
                return Math.sin(x) - x / 2;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                // Define the convergence criteria here
                if (Math.abs(f0) < 1e-10) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-10) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        try {
            assertTrue(solver.findRoot(function, checker, 100, 0, -0.5, Math.PI, 1));
            assertEquals(1.8954942670339816, solver.getRoot(), 1e-10);
        } catch (FunctionException e) {
            fail("Exception occurred");
        }
    }
    
    // Additional test cases for edge cases and exceptions.
    
    // Test when the function value at the lower bound is zero
    @Test
    public void testFindRootForFunctionValueAtLowerBoundZero() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.cos(x);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-10) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-10) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        try {
            assertTrue(solver.findRoot(function, checker, 100, 0, 0, Math.PI, 1));
            assertEquals(0.0, solver.getRoot(), 1e-10);
        } catch (FunctionException e) {
            fail("Exception occurred");
        }
    }
    
    // Test when the function value at the higher bound is zero
    @Test
    public void testFindRootForFunctionValueAtHigherBoundZero() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-10) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-10) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        try {
            assertTrue(solver.findRoot(function, checker, 100, 0, -1, Math.PI, 0));
            assertEquals(Math.PI, solver.getRoot(), 1e-10);
        } catch (FunctionException e) {
            fail("Exception occurred");
        }
    }
    
    // Test when the function value at both bounds is zero
    @Test
    public void testFindRootForFunctionValueAtBothBoundsZero() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x) * Math.cos(x);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-10) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-10) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        try {
            assertTrue(solver.findRoot(function, checker, 100, -1, 0, Math.PI, 0));
            assertEquals(-0.5 * Math.PI, solver.getRoot(), 1e-10);
        } catch (FunctionException e) {
            fail("Exception occurred");
        }
    }
}
