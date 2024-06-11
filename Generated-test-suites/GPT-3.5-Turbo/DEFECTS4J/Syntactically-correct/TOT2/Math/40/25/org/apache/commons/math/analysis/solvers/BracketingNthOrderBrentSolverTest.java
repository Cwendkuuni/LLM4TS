import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertNotNull(solver);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 1e-12, 9);
        assertNotNull(solver);
        assertEquals(1e-10, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-12, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(9, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, Math::sin, 0.0, Math.PI, AllowedSolution.RIGHT_SIDE);
        assertEquals(Math.PI, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, Math::cos, 0.0, 3 * Math.PI, 2.5, AllowedSolution.LEFT_SIDE);
        assertEquals(3 * Math.PI, result, 1e-6);
    }

}