import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x*x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 3.0;
        double f1 = 5.0;

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 2;
            }
        }, new ConvergenceChecker(), 100, 0.0, -1.0, 3.0, 1.0);

        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootException() {
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Function evaluation error");
            }
        }, new ConvergenceChecker(), 100, 0.0, -1.0, 3.0, 1.0);
    }
}