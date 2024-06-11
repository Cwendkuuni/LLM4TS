import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x*x - 4; // Function x^2 - 4 where root is 2
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 4, 12);
        assertTrue(rootFound);
        assertEquals(2, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

}