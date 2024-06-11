import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, -10, 10);
            assertEquals(2.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, -10, 10, 0.001);
            assertEquals(2.0, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
            assertEquals(-2.0, result[0], 0.0001);
            assertEquals(2.0, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10, 100);
            assertEquals(-2.0, result[0], 0.0001);
            assertEquals(2.0, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMidpoint() {
        double a = -10.0;
        double b = 10.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(0.0, result, 0.0001);
    }
}