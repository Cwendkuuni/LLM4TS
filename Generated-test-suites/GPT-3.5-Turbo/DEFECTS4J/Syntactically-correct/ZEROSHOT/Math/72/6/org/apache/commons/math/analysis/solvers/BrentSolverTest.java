import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunctionMock;
import org.spaceroots.mantissa.roots.ConvergenceCheckerMock;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1
        double x0 = 0.0;
        double f0 = 2.0;
        double x1 = 1.0;
        double f1 = -1.0;

        ComputableFunctionMock function = new ComputableFunctionMock();
        ConvergenceCheckerMock checker = new ConvergenceCheckerMock();

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(0.0, solver.getRoot(), EPSILON);

        // Test case 2
        x0 = -2.0;
        f0 = 2.0;
        x1 = -1.0;
        f1 = 1.0;

        rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(-1.0, solver.getRoot(), EPSILON);

        // Test case 3
        x0 = -2.0;
        f0 = -2.0;
        x1 = -1.0;
        f1 = 1.0;

        rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(-1.0, solver.getRoot(), EPSILON);

        // Test case 4
        x0 = -2.0;
        f0 = 2.0;
        x1 = -1.0;
        f1 = -1.0;

        rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertFalse(rootFound);
    }
}