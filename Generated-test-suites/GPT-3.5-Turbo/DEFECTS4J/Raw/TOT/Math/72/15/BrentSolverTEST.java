import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_typical() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 16; // x^2 - 16 = 0
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return 0;
            }
        };
        double x0 = 0.0;
        double f0 = -16.0;
        double x1 = 5.0;
        double f1 = 9.0;

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(4.0, solver.getRoot(), 0.000001);
    }

    @Test
    public void testFindRoot_edge() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x); // sin(x) = 0
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return 0;
            }
        };
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = Math.PI;
        double f1 = 0.0;

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(Math.PI, solver.getRoot(), 0.000001);
    }

    @Test
    public void testFindRoot_error() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                throw new FunctionException("Error evaluating function");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return 0;
            }
        };
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 5.0;
        double f1 = 10.0;

        BrentSolver solver = new BrentSolver();
        try {
            solver.findRoot(function, checker, 10, x0, f0, x1, f1);
            fail("Expected FunctionException");
        } catch (FunctionException e) {
            assertEquals("Error evaluating function", e.getMessage());
        }
    }

}