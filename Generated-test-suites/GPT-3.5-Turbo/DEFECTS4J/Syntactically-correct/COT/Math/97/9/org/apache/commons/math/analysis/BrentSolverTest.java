import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.Constant;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws Exception {
        Constant function = new Constant(5.0); // create a constant function
        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, 0, -5, 10, 5); // test with arbitrary values
        assertTrue(result);
        assertEquals(5.0, solver.getRoot(), 0.0001); // check if root is correct
    }

    @Test
    public void testGetRoot() throws Exception {
        Constant function = new Constant(0.0);
        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, -10, -5, 5, 5); // test with same values for lower and higher bounds
        assertTrue(result);
        assertEquals(0.0, solver.getRoot(), 0.0001);
    }

}