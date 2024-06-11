import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ERROR = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return 3 * x - 25;
            }
        };

        double result = solver.solve(100, null, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(8.333333, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolveWithInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 2;
            }
        };

        double result = solver.solve(100, null, 0, 10, 1, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(1.414213, result, ABSOLUTE_ERROR);
    }
}