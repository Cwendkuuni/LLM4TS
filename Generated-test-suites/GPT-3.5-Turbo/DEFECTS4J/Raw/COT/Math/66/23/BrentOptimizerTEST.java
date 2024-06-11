import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
            assertEquals(0, result, 1E-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, -1, 1, 0.5);
            assertEquals(0, result, 1E-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithMaxIterationsExceededException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            optimizer.setMaximalIterationCount(10);
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        } catch (MaxIterationsExceededException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNegativeRelativeAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            optimizer.setRelativeAccuracy(-1);
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeWithNegativeAbsoluteAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            optimizer.setAbsoluteAccuracy(-1);
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

}