import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.0001;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, actual, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 1;
        double upperBound = 3;
        double[] expected = {1, 3};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(expected, actual, EPSILON);
    }

    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 1;
        double upperBound = 3;
        int maximumIterations = 10;
        double[] expected = {1, 3};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertArrayEquals(expected, actual, EPSILON);
    }

    @Test
    public void testMidpoint() {
        double a = 1;
        double b = 3;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, actual, EPSILON);
    }
}