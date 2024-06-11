import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_withDefaultParameters() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };
        double min = -10;
        double max = 10;
        double expectedOptimum = 0;
        double actualOptimum = optimizer.optimize(f, GoalType.MINIMIZE, min, max);

        assertEquals(expectedOptimum, actualOptimum, 1e-10);
    }

    @Test
    public void testOptimize_withStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2, 2) + 1;
            }
        };
        double min = -10;
        double max = 10;
        double startValue = 5;
        double expectedOptimum = 2;
        double actualOptimum = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(expectedOptimum, actualOptimum, 1e-10);
    }

    @Test
    public void testOptimize_withMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double min = -10;
        double max = 10;

        try {
            optimizer.optimize(f, GoalType.MINIMIZE, min, max);
            fail("Expected MaxIterationsExceededException was not thrown.");
        } catch (MaxIterationsExceededException e) {
            // Test passed
        }
    }

    @Test
    public void testOptimize_withFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    throw new FunctionEvaluationException("Cannot evaluate function at x = 0");
                }
                return 1 / x;
            }
        };
        double min = -10;
        double max = 10;

        try {
            optimizer.optimize(f, GoalType.MINIMIZE, min, max);
            fail("Expected FunctionEvaluationException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Test passed
        }
    }

}