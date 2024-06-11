
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            double expected = 2.0;
            assertEquals(expected, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0001;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            double expected = 2.0;
            assertEquals(expected, result, absoluteAccuracy);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            double[] expected = {1.0, 3.0};
            assertArrayEquals(expected, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            double[] expected = {1.0, 3.0};
            assertArrayEquals(expected, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testMidpoint() {
        double a = -2.0;
        double b = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        double expected = 0.0;
        assertEquals(expected, result, 0.0001);
    }
}
