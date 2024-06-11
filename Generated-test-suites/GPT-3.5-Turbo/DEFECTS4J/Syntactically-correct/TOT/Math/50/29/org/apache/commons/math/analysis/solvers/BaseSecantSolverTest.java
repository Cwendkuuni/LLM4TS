import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private class DummyFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x - 5;
        }
    }

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new DummySolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @After
    public void tearDown() {
        solver = null;
    }

    @Test
    public void testSolve_WithSolutionWithinAccuracy_ReturnsApproximatedRoot() {
        UnivariateRealFunction dummyFunc = new DummyFunction();

        double result = solver.solve(100, dummyFunc, 0, 10, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_WithNoBracketingRange_ThrowsNoBracketingException() {
        UnivariateRealFunction dummyFunc = new DummyFunction();

        double result = solver.solve(100, dummyFunc, 0, 2, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_WithFunctionValueBelowTolerance_ReturnsApproximatedRoot() {
        UnivariateRealFunction dummyFunc = new DummyFunction();

        double result = solver.solve(100, dummyFunc, 0, 10, 4, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueAboveTolerance_ReturnsApproximatedRoot() {
        UnivariateRealFunction dummyFunc = new DummyFunction();

        double result = solver.solve(100, dummyFunc, 0, 10, 6, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithLeftSideSolutionAndInvertedInterval_ReturnsApproximatedRoot() {
        class InvertedFunction implements UnivariateRealFunction {
            public double value(double x) {
                return -(x - 5);
            }
        }

        UnivariateRealFunction invertedFunc = new InvertedFunction();

        double result = solver.solve(100, invertedFunc, 0, 10, 6, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithRightSideSolutionAndInvertedInterval_ReturnsApproximatedRoot() {
        class InvertedFunction implements UnivariateRealFunction {
            public double value(double x) {
                return -(x - 5);
            }
        }

        UnivariateRealFunction invertedFunc = new InvertedFunction();

        double result = solver.solve(100, invertedFunc, 0, 10, 4, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

}