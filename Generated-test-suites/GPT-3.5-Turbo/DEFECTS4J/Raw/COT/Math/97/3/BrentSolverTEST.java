import org.junit.*;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
                if (fLow == 0.0) {
                    return ConvergenceChecker.LOW;
                } else if (fHigh == 0.0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOTHING;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 2.0;
        double f1 = 0.0;
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-8);
    }

    @Test
    public void testFindRootWithException() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function evaluation error");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
                return ConvergenceChecker.NOTHING;
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 2.0;
        double f1 = 0.0;
        
        boolean exceptionThrown = false;
        try {
            solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        } catch (FunctionException e) {
            exceptionThrown = true;
            assertEquals("Function evaluation error", e.getMessage());
        }

        assertTrue(exceptionThrown);
    }

}