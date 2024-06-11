import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve_WithRootInRange_ReturnsRoot() {
        double result = solver.solve(100, x -> x - 2, -10, 10);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithRootAtBoundary_ReturnsBoundary() {
        double result = solver.solve(100, x -> x - 2, -10, 2);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithoutRoot_ThrowsNoBracketingException() {
        try {
            solver.solve(100, x -> x - 2, -10, -5);
            fail("NoBracketingException expected");
        } catch (NoBracketingException e) {
            assertEquals(-10, e.getMin(), 1e-6);
            assertEquals(-5, e.getMax(), 1e-6);
        }
    }

    @Test
    public void testSolve_WithMaximalOrderEquals2_DoesNotThrowError() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 2);
        double result = solver.solve(100, x -> x - 2, -10, 10);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_WithMaximalOrderLessThan2_ThrowsNumberIsTooSmallException() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testGetMaximalOrder_ReturnsMaximalOrder() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(5, solver.getMaximalOrder());
    }
}