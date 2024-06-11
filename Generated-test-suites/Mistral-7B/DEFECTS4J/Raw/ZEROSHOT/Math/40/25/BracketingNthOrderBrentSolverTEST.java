import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() {
        // Test default constructor
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 2) + 1;
        double root = solver.solve(10, function, -1, 1);
        assertEquals(0, root, 1e-6);

        // Test constructor with specified accuracy and maximal order
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-7, 6);
        assertEquals(1e-7, solver1.getAbsoluteAccuracy(), 1e-8);
        assertEquals(6, solver1.getMaximalOrder());
        double root1 = solver1.solve(10, function, -1, 1);
        assertEquals(0, root1, 1e-7);

        // Test constructor with relative accuracy and maximal order
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-5, 1e-6, 6);
        assertEquals(1e-5, solver2.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-6, solver2.getAbsoluteAccuracy(), 1e-8);
        assertEquals(6, solver2.getMaximalOrder());
        double root2 = solver2.solve(10, function, -1, 1);
        assertEquals(0, root2, 1e-6);

        // Test constructor with relative accuracy, absolute accuracy, and maximal order
        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver(1e-5, 1e-7, 6);
        assertEquals(1e-5, solver3.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-7, solver3.getAbsoluteAccuracy(), 1e-8);
        assertEquals(6, solver3.getMaximalOrder());
        double root3 = solver3.solve(10, function, -1, 1);
        assertEquals(0, root3, 1e-7);

        // Test constructor with too low maximal order
        try {
            new BracketingNthOrderBrentSolver(1e-6, 1);
            org.junit.Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test solve with allowed solution
        BracketingNthOrderBrentSolver solver4 = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver4.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        double root4 = solver4.solve(10, function, -1, 1);
        assertEquals(-1, root4, 1e-6);

        solver4.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        root4 = solver4.solve(10, function, -1, 1);
        assertEquals(1, root4, 1e-6);

        // Test solve with unsupported function
        BracketingNthOrderBrentSolver solver5 = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function2 = null;
        try {
            solver5.solve(10, function2, -1, 1);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

    }

}