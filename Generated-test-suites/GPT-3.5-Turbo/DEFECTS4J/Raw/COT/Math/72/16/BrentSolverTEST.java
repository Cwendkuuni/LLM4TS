
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = 0;
        double f0 = 1;
        double x1 = 2;
        double f1 = -1;

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * (x - 1);
            }
        };

        boolean foundRoot = solver.findRoot(function, null, 100, x0, f0, x1, f1);
        assertTrue(foundRoot);
        assertEquals(1.0, solver.getRoot(), 1e-10);
    }
}
