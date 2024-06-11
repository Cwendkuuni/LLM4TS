import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalers.SquaredDifferenceScalingFunction;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        // This method should not be called directly and throws an UnsupportedOperationException
        try {
            BrentOptimizer optimizer = new BrentOptimizer();
            optimizer.doOptimize();
            Assert.fail("Method doOptimize should not be called directly.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedResult = 0.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedResult = 1.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMinWithGoalTypeMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
            Assert.fail("Expected FunctionEvaluationException when GoalType is MAXIMIZE.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeMaxWithGoalTypeMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            Assert.fail("Expected FunctionEvaluationException when GoalType is MINIMIZE.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeMinWithMinLessThanMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        double expectedResult = 1.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMinWithMinGreaterThanMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 2.0;
        double max = 1.0;
        double startValue = 1.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            Assert.fail("Expected IllegalArgumentException when min > max.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeMaxWithMinGreaterThanMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 2.0;
        double max = 1.0;
        double startValue = 1.5;

        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
            Assert.fail("Expected IllegalArgumentException when min > max.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeMinWithZeroAsBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 0.0;
        double startValue = 0.5;
        double expectedResult = 0.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMaxWithZeroAsBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 0.0;
        double startValue = 0.5;
        double expectedResult = 0.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMinWithNegativeBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedResult = 0.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeMaxWithNegativeBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.5;
        double expectedResult = 0.0;
        double tolerance = 1E-7;

        optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, optimizer.getResult().getValue(), tolerance);
    }

    @Test
    public void testOptimizeWithInfiniteBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 0.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            Assert.fail("Expected IllegalArgumentException when bounds are not finite.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionThrowingException("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            Assert.fail("Expected MaxIterationsExceededException when function throws FunctionEvaluationException.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        UnivariateRealFunction function = new FunctionSquareRoot("x^2");
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            Assert.fail("Expected MaxIterationsExceededException when maximal iteration count is 1.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    private static class FunctionSquareRoot implements UnivariateRealFunction {
        private final String functionName;

        public FunctionSquareRoot(String functionName) {
            this.functionName = functionName;
        }

        @Override
        public double value(double x) {
            System.out.println("Function " + functionName + " evaluated at x = " + x);
            return x * x;
        }
    }

    private static class FunctionThrowingException implements UnivariateRealFunction {
        private final String functionName;

        public FunctionThrowingException(String functionName) {
            this.functionName = functionName;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            System.out.println("Function " + functionName + " evaluated at x = " + x);
            if (Math.abs(x - 1.0) < 1E-6) {
                throw new FunctionEvaluationException("FunctionEvaluationException: Function evaluation failed.");
            }
            return x * x;
        }
    }
}