import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidFunction_ExpectedRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, function, -100, 100);

        Assert.assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidFunction_ThrowsConvergenceException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Double.POSITIVE_INFINITY;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double result = solver.solve(100, function, -100, 100);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // test passed
        }
    }

    @Test
    public void testSolve_WithValidFunction_AllowedLeftSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, function, -100, 100, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        Assert.assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithValidFunction_AllowedRightSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, function, -100, 100, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithValidFunction_AllowedBelowSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, function, -100, 100, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        Assert.assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithValidFunction_AllowedAboveSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, function, -100, 100, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        Assert.assertEquals(4.0, result, 1e-6);
    }

}