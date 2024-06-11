import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Typical case - root found in the given interval
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(new ComputableFunctionImplementation(),
                                           new ConvergenceCheckerImplementation(),
                                           100, 0.0, -1.0, 2.0, 2.0);
        assertTrue(rootFound);
    }

    @Test
    public void testGetRoot() {
        // Typical case - get the abscissa of the root
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunctionImplementation(),
                          new ConvergenceCheckerImplementation(),
                          100, 0.0, -1.0, 2.0, 2.0);
        double root = solver.getRoot();
        assertEquals(1.0, root, 0.0001);
    }

    // Additional edge cases and exception handling scenarios can be added here

}