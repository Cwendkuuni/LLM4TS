import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {
    BrentOptimizer optimizer;
    UnivariateRealFunction f;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testOptimize() {
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            assertEquals("Unexpected result", 1.999, result, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test edge cases
    @Test
    public void testOptimizeWithEqualBounds() {
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 2, 2);
            assertEquals("Unexpected result", 2, result, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithMaxIterations() {
        try {
            optimizer.setMaximalIterationCount(1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveRelativeAccuracy() {
        try {
            optimizer.setRelativeAccuracy(-1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveAbsoluteAccuracy() {
        try {
            optimizer.setAbsoluteAccuracy(-1);
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test exceptions
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.doOptimize();
    }
}