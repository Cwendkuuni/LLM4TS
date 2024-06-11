import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeRegistry;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        // Test with a function that has a known minimum.
        UnivariateRealFunction testFunction = new TestFunction();
        GoalType goalType = GoalTypeRegistry.valueOf("MINIMIZE");
        double min = 1.0;
        double max = 3.0;
        double initialGuess = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, goalType, min, max, initialGuess);

        double expectedMinimum = 1.5;
        assertEquals(expectedMinimum, result.getPoint(), 0.001);
        assertEquals(expectedMinimum, result.getValue(), 0.001);

        // Test with a function that has a known maximum.
        goalType = GoalTypeRegistry.valueOf("MAXIMIZE");
        UnivariateRealFunction maxTestFunction = new MaxTestFunction();

        double minForMax = -3.0;
        double maxForMax = 3.0;
        double initialGuessForMax = 0.0;

        PointValuePair maxResult = optimizer.optimize(maxTestFunction, goalType, minForMax, maxForMax, initialGuessForMax);

        double expectedMaximum = 2.0;
        assertEquals(expectedMaximum, maxResult.getPoint(), 0.001);
        assertEquals(expectedMaximum, maxResult.getValue(), 0.001);

        // Test with a non-convex function.
        UnivariateRealFunction nonConvexFunction = new NonConvexFunction();

        double minForNonConvex = -5.0;
        double maxForNonConvex = 5.0;
        double initialGuessForNonConvex = 0.0;

        try {
            optimizer.optimize(nonConvexFunction, goalType, minForNonConvex, maxForNonConvex, initialGuessForNonConvex);
            Assert.fail("Expected MaxIterationsExceededException not thrown.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function that does not have a minimum or maximum.
        UnivariateRealFunction noMinMaxFunction = new NoMinMaxFunction();

        try {
            optimizer.optimize(noMinMaxFunction, goalType, min, max, initialGuess);
            Assert.fail("Expected FunctionEvaluationException not thrown.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    /**
     * A test function with a known minimum at 1.5.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 1.5, 2);
        }
    }

    /**
     * A test function with a known maximum at 2.0.
     */
    private static class MaxTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x + 1, 2);
        }
    }

    /**
     * A test function that is not convex.
     */
    private static class NonConvexFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 0) {
                return Math.pow(Math.sin(Math.PI * x), 2);
            } else {
                return Math.pow(-Math.sin(Math.PI * x), 2);
            }
        }
    }

    /**
     * A test function that does not have a minimum or maximum.
     */
    private static class NoMinMaxFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 0) {
                return Math.random();
            } else {
                return -Math.random();
            }
        }
    }
}