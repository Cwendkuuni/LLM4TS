import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double x0 = -2;
        double x1 = 2;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        assert Math.abs(result - 2) < 0.0001;
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double x0 = -2;
        double x1 = 2;
        double absoluteAccuracy = 0.0001;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);

        assert Math.abs(result - 2) < 0.0001;
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double initial = 0;
        double lowerBound = -3;
        double upperBound = 3;

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);

        assert result[0] == -1;
        assert result[1] == 1;
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double initial = 0;
        double lowerBound = -3;
        double upperBound = 3;
        int maximumIterations = 10;

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);

        assert result[0] == -1;
        assert result[1] == 1;
    }

    @Test
    public void testMidpoint() {
        double a = 2;
        double b = 6;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assert result == 4;
    }
}