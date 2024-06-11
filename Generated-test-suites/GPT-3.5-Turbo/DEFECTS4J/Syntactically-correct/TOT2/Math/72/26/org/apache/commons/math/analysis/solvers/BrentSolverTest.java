import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot_basicCase() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 4;
            }
        };
        boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 1, -3, 2, 0);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRoot_edgeCases() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x*x - 27;
            }
        };
        boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -27, 4, 53);
        assertTrue(rootFound);
        assertEquals(3.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRoot_exceptionHandling() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                if (x == 0) {
                    throw new FunctionException("Division by zero");
                }
                return 1 / x;
            }
        };
        try {
            solver.findRoot(function, new ConvergenceChecker(), 100, 0, -1, 2, 0.5);
            fail("Expected FunctionException to be thrown");
        } catch (FunctionException e) {
            // Exception caught, test passed
        }
    }

}