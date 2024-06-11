import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, -1.5, 1.5);
        assertNotNull(result);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test error");
            }
        };
        UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgument() {
        UnivariateRealFunction function = null;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
        });
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-5);
        assertNotNull(result);
        assertEquals(0.0, result, 5e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return 1.0 / (x * Math.log(Math.abs(x)));
                } else {
                    return -1.0 / (Math.abs(x) * Math.log(Math.abs(x)));
                }
            }
        };
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(function, 0.0, 0.1, 1e-20);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidArgument() {
        UnivariateRealFunction function = new UnivariateRealFunction() {};
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, 0.0, 1.0, 1.0);
        });
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(function, 1.0, -1.0, 1.0);
        assertNotNull(brackets);
        assertEquals(-1.0, brackets[0], 1e-6);
        assertEquals(1.0, brackets[1], 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return 1.0 / (x * Math.log(Math.abs(x)));
                } else {
                    return -1.0 / (Math.abs(x) * Math.log(Math.abs(x)));
                }
            }
        };
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.1, 1.0, 1000);
        });
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketWithFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test error");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 1000);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithIllegalArgument() {
        UnivariateRealFunction function = null;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 1000);
        });
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - Math.pow(Math.cos(x), 2);
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(function, 0.0, Math.PI, -Math.PI, 10);
        assertNotNull(brackets);
        assertEquals(-1.0, brackets[0], 1e-6);
        assertEquals(1.0, brackets[1], 1e-6);
    }

    @Test
    public void testMidpoint() {
        double a = 2.5;
        double b = 3.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(3.0, result, 1e-6);
    }
}