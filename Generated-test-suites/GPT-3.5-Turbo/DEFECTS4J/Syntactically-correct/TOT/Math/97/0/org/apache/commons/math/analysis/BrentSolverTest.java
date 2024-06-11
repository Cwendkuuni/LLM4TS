import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    private BrentSolver solver;
    private double epsilon;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        epsilon = Math.pow(2.0, -52); // IEEE 754 epsilon
    }

    @Test
    public void testFindRoot_typicalCase() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = 0.0;
        double f0 = Math.sin(x0);
        double x1 = 1.0;
        double f1 = Math.sin(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_edgeCase() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4; // equation: x^2 - 4 = 0, roots: -2, 2
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = -3.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(-2.0, solver.getRoot(), epsilon);

        x0 = -1.0;
        f0 = function.valueAt(x0);
        x1 = 2.0;
        f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_errorScenario() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function evaluation error");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.NONE;
            }
        };

        int maxIter = 100;
        double x0 = -1.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);

        try {
            solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            fail("FunctionException not thrown");
        } catch (FunctionException e) {
            // Exception expected
        }
    }

}