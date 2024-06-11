import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void solve_defaultSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    throw new FunctionEvaluationException(x);
                }
                return x - 1;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 2);

        Assert.assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void solve_customSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    throw new FunctionEvaluationException(x);
                }
                return x - 1;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 2, 0.0001);

        Assert.assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void bracket_validParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0) {
                    throw new FunctionEvaluationException(x);
                }
                return x - 1;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -1, 1);

        Assert.assertEquals(-1.0, result[0], 0.0001);
        Assert.assertEquals(1.0, result[1], 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void bracket_invalidParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0) {
                    throw new FunctionEvaluationException(x);
                }
                return x - 1;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, 1, -1);
    }

    @Test
    public void midpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);

        Assert.assertEquals(2.0, result, 0.0001);
    }

}