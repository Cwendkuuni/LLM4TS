import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve_maxEval() {
        try {
            solver.solve(-1, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("IllegalArgumentException not thrown for negative maxEval");
        } catch (IllegalArgumentException ex) {
            // expected behavior
        }
    }

    @Test
    public void testSolve_minGreaterMax() {
        try {
            solver.solve(100, function, 10, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("IllegalArgumentException not thrown for min > max");
        } catch (IllegalArgumentException ex) {
            // expected behavior
        }
    }

    @Test
    public void testSolve_noBracket() {
        try {
            solver.solve(100, function, -2, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
            fail("MathInternalError not thrown for no bracketing");
        } catch (MathInternalError ex) {
            // expected behavior
        }
    }

    @Test
    public void testSolve_exactRoot() {
        double result = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testSolve_belowSolutionAnySide() {
        double result = solver.solve(100, function, -10, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolve_aboveSolutionAnySide() {
        double result = solver.solve(100, function, 10, 20, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_belowsolutionBelowSide() {
        try {
            solver.solve(100, function, -10, 0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
            fail("MathInternalError not thrown for no solution below side");
        } catch (MathInternalError ex) {
            // expected behavior
        }
    }

    @Test
    public void testSolve_abovesolutionAboveSide() {
        try {
            solver.solve(100, function, 10, 20, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("MathInternalError not thrown for no solution above side");
        } catch (MathInternalError ex) {
            // expected behavior
        }
    }

    @Test
    public void testSolve_solutionLeftSide() {
        double result = solver.solve(100, function, -10, 0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolve_solutionRightSide() {
        double result = solver.solve(100, function, 10, 20, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

}