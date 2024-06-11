import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 3.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 3.0, 1.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testOptimizeWithInvalidEps() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 3.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testOptimizeWithInvalidT() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 3.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }
}