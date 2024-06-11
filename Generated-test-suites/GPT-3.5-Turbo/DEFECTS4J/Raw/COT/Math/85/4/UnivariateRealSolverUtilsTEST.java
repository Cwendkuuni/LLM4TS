
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 3;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(2, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 3;
        double absoluteAccuracy = 0.0001;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(2, result, absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertEquals(0, result[0], 0.0001);
            assertEquals(3, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testBracketWithIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        int maximumIterations = 100;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            assertEquals(0, result[0], 0.0001);
            assertEquals(3, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testMidpoint() {
        double a = -2;
        double b = 2;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(0, result, 0.0001);
    }
}
