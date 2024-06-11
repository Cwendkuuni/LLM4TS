import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private class DummyFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4;
        }
    }

    private BaseSecantSolver solver;

    @Before
    public void setUp() throws Exception {
        solver = new DummySecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_withValidSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolve_withMinEqualsRoot_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 2, 5, AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolve_withMaxEqualsRoot_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 2, AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withNoRootInRange_shouldThrowConvergenceException() throws Exception {
        solver.solve(100, new DummyFunction(), 5, 10, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_withAnySideSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolve_withLeftSideSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.LEFT_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withLeftSideSolution_whenRootOnRightSide_shouldThrowConvergenceException() throws Exception {
        solver.solve(100, new DummyFunction(), 2, 5, AllowedSolution.LEFT_SIDE);
    }

    @Test
    public void testSolve_withRightSideSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.RIGHT_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withRightSideSolution_whenRootOnLeftSide_shouldThrowConvergenceException() throws Exception {
        solver.solve(100, new DummyFunction(), 0, 2, AllowedSolution.RIGHT_SIDE);
    }

    @Test
    public void testSolve_withBelowSideSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.BELOW_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withBelowSideSolution_whenRootAbove_shouldThrowConvergenceException() throws Exception {
        solver.solve(100, new DummyFunction(), 0, 1, AllowedSolution.BELOW_SIDE);
    }

    @Test
    public void testSolve_withAboveSideSolution_shouldReturnRoot() throws Exception {
        double result = solver.solve(100, new DummyFunction(), 0, 5, AllowedSolution.ABOVE_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withAboveSideSolution_whenRootBelow_shouldThrowConvergenceException() throws Exception {
        solver.solve(100, new DummyFunction(), 3, 5, AllowedSolution.ABOVE_SIDE);
    }

}