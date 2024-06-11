import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return 0;
            }
        };

        try {
            boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
            assertTrue(result);
            assertEquals(0, solver.getRoot(), 0.0001);
        } catch (FunctionException e) {
            fail("FunctionException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindRoot_noRoot() {
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return 1;
            }
        };

        try {
            boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
            assertFalse(result);
        } catch (FunctionException e) {
            fail("FunctionException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindRoot_invalidInterval() {
        double x0 = 1.0;
        double f0 = 2.0;
        double x1 = 0.0;
        double f1 = 1.0;

        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return 0;
            }
        };

        try {
            boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
            assertFalse(result);
        } catch (FunctionException e) {
            fail("FunctionException thrown: " + e.getMessage());
        }
    }
}