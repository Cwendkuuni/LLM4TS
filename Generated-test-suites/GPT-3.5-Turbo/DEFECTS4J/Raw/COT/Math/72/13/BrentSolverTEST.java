
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

    // Test findRoot method with a simple linear function
    @Test
    public void testFindRootWithLinearFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x - 2.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(x1 - x0) < BrentSolver.epsilon) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NONE;
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 1000, 0, -2, 10, 18);
        assertTrue(rootFound);

        double root = solver.getRoot();
        assertEquals(2.0, root, 0.0);
    }

    // Test findRoot method with a quadratic function
    @Test
    public void testFindRootWithQuadraticFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f1) < BrentSolver.epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                if (Math.abs(f0) < BrentSolver.epsilon) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NONE;
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 1000, -10, 96, 10, -96);
        assertTrue(rootFound);

        double root = solver.getRoot();
        assertEquals(2.0, root, 0.0);
    }

    // Test findRoot method with a function that should not converge
    @Test
    public void testFindRootWithNonConvergingFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.exp(-x) * Math.sin(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                return ConvergenceChecker.NONE;
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 1000, 0, 1, 10, -1);
        assertFalse(rootFound);
    }

    // Test findRoot method with a function that throws an exception
    @Test(expected = FunctionException.class)
    public void testFindRootWithExceptionThrowingFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function exception");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                return ConvergenceChecker.NONE;
            }
        };

        solver.findRoot(function, checker, 1000, 0, 1, 10, -1);
    }

    // Additional test cases for edge cases and exceptions
    // ...

}
