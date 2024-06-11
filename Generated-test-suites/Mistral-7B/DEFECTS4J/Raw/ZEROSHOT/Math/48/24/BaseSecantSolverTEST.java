import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.SingularMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        function = f -> Math.pow(Math.E, Math.sin(Math.PI * f / 2));
    }

    @Test
    public void testSolve() {
        try {
            double solution = solver.solve(10, function, -1, 1);
            assertEquals(Math.PI / 2, solution, 1e-7);
        } catch (ConvergenceException e) {
            fail("The algorithm should have converged.");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveNotConverged() {
        solver = new BaseSecantSolver(1e-16, BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, function, -1, 1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidFunction() {
        function = null;
        solver.solve(10, function, -1, 1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidMin() {
        function = f -> f * f - 4;
        solver.solve(10, function, -1, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidMax() {
        function = f -> f * f - 4;
        solver.solve(10, function, 1, 0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSolveSingularMatrix() {
        function = f -> {
            double[][] coefficients = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
            double[] constants = new double[]{0.0, 0.0};
            try {
                return org.apache.commons.math3.linear.MatrixUtils.solve(coefficients, constants);
            } catch (Exception e) {
                throw new SingularMatrixException("Failed to invert matrix.");
            }
        };
        solver.solve(10, function, 0, 0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveInternalError() {
        function = f -> {
            throw new RuntimeException("Something went wrong.");
        };
        solver.solve(10, function, 0, 0);
    }

    @Test
    public void testAllowedSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(10, function, -1, 1);
        assertEquals(Math.PI / 2, solution, 1e-7);
        assertTrue(solver.isRootInsideInterval());
    }

    @Test
    public void testAllowedSolutionBelow() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double solution = solver.solve(10, function, -1, 1);
        assertEquals(Math.PI / 2, solution, 1e-7);
        assertFalse(solver.isRootInsideInterval());
    }

    @Test
    public void testAllowedSolutionAbove() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        double solution = solver.solve(10, function, -1, 1);
        assertEquals(Math.PI / 2, solution, 1e-7);
        assertFalse(solver.isRootInsideInterval());
    }

    @Test
    public void testAllowedSolutionRight() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(10, function, -1, 1);
        assertEquals(Math.PI / 2, solution, 1e-7);
        assertFalse(solver.isRootInsideInterval());
    }
}