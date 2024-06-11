
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve_WhenFunctionIsLinear_AndAllowedSolutionIsAnySide_ThenReturnCorrectRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2 * x - 6;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(3, root, EPSILON);
    }

    @Test
    public void testSolve_WhenFunctionHasMultipleRoots_AndAllowedSolutionIsAnySide_ThenReturnAnyRootWithinGivenBoundaries() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertTrue(root == -2 || root == 2);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_WhenFunctionDoesNotConverge_ThenThrowConvergenceException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.tan(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        solver.solve(100, function, 0.5, 3.5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    }

    @Test
    public void testSolve_WhenFunctionHasRootsOutsideOfGivenBoundaries_AndAllowedSolutionIsAnySide_ThenReturnClosestRootWithinGivenBoundaries() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, function, 4.5, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(2, root, EPSILON);
    }

    @Test
    public void testSolve_WhenFunctionHasRootsInTheBoundaries_OfOtherRoots_AndAllowedSolutionIsRightSide_ThenReturnRightRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, function, -2, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(2, root, EPSILON);
    }
}

