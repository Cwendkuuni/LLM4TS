import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4; // Function x^2 - 4 has roots at x = -2 and x = 2
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 1000;
        double x0 = -3;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x + 1; // Function x^2 + 1 has no roots
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 1000;
        double x0 = -3;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }
}