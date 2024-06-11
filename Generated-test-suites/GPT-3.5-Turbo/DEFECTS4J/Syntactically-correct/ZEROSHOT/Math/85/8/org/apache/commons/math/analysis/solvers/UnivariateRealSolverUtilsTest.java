import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 3);
        assertEquals(2, result, 0.0001);

        result = UnivariateRealSolverUtils.solve(f, 0, 3, 0.0001);
        assertEquals(2, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.solve(f, 0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidEndpoints() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 3, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 0, 1);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
        assertEquals(-3, result[0], 0.0001);
        assertEquals(3, result[1], 0.0001);

        result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10, 20);
        assertEquals(-3, result[0], 0.0001);
        assertEquals(3, result[1], 0.0001);

        result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10, 5);
        assertEquals(-3, result[0], 0.0001);
        assertEquals(3, result[1], 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidEndpoints() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, 10, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, -10, 10, -1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 0, 1, 2);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2, result, 0.0001);
    }
}