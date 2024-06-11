import org.junit.Test;
import org.junit.Assert;

public class BaseSecantSolverTest {
    private static final double ABSOLUTE_ERROR = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, AllowedSolution.ANY_SIDE);

        Assert.assertEquals(2.0, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolveWithIncorrectInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 3, 1, AllowedSolution.ANY_SIDE);

        Assert.assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolveWithZeroFunctionValue() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        }, -10, 10, AllowedSolution.ANY_SIDE);

        Assert.assertEquals(0.0, result, ABSOLUTE_ERROR);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceFailure() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        solver.solve(5, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveWithMaxEvalReached() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double result = solver.solve(5, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3, AllowedSolution.ANY_SIDE);

        Assert.assertTrue(Double.isNaN(result));
    }
}