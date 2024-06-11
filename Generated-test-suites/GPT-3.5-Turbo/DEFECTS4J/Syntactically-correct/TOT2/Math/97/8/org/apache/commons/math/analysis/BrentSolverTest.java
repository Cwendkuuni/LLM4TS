import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 5.0);

        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x;
            }
        }, new ConvergenceChecker(), 100, -1.0, -1.0, 1.0, 1.0);

        assertEquals(solver.getRoot(), 0.0, 1e-10);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}