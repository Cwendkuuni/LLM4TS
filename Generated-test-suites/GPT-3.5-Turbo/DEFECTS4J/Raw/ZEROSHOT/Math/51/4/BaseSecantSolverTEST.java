import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;
import org.junit.Assert;

public class BaseSecantSolverTest {

    private double absoluteAccuracy = 1e-6;
    private double relativeAccuracy = 1e-6;
    private double functionValueAccuracy = 1e-15;

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 4;
        }
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);

        TestFunction function = new TestFunction();

        double result = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals("Solution not found within required accuracy", 2.0, result, absoluteAccuracy);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);

        TestFunction function = new TestFunction();

        double result = solver.solve(100, function, 0, 10, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals("Solution not found within required accuracy", 2.0, result, absoluteAccuracy);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);

        TestFunction function = new TestFunction();

        double result = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    }
}