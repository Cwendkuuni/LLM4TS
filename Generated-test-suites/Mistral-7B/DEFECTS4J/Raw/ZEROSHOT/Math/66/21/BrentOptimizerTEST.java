import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.PointsToTest;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test minimization with given bounds
        double min = 1.0;
        double max = 5.0;
        double expectedResult = 2.0;
        double result = 0.0;

        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, 3.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }

        Assert.assertEquals(expectedResult, result, 0.001);

        // Test maximization with given bounds
        double maxResult = 0.0;
        try {
            maxResult = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, 3.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }

        Assert.assertEquals(expectedResult, maxResult, 0.001);

        // Test minimization without bounds
        result = 0.0;
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 3.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }

        Assert.assertEquals(expectedResult, result, 0.001);

        // Test maximization without bounds
        maxResult = 0.0;
        try {
            maxResult = optimizer.optimize(function, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 3.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }

        Assert.assertEquals(expectedResult, maxResult, 0.001);

        // Test exception when eps and t are not positive
        try {
            BrentOptimizer optimizerNegativeEps = new BrentOptimizer();
            optimizerNegativeEps.setRelativeAccuracy(-0.01);
            optimizerNegativeEps.optimize(function, GoalType.MINIMIZE, min, max, 3.0);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("Relative accuracy must be positive", e.getMessage());
        }

        try {
            BrentOptimizer optimizerNegativeT = new BrentOptimizer();
            optimizerNegativeT.setAbsoluteAccuracy(-0.01);
            optimizerNegativeT.optimize(function, GoalType.MINIMIZE, min, max, 3.0);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("Absolute accuracy must be positive", e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        UnivariateRealFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 1.0, 5.0, 3.0);
        } catch (FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new BrokenFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 1.0, 5.0, 3.0);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testPointsToTest() {
        UnivariateRealFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer();
        PointsToTest pointsToTest = new PointsToTest();

        double min = 1.0;
        double max = 5.0;
        double expectedResult = 2.0;
        double result = 0.0;

        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, 3.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Test failed: " + e.getMessage());
        }

        Assert.assertEquals(expectedResult, result, 0.001);

        pointsToTest.addTestPoint(new double[]{1.1, 2.1, 3.1}, new double[]{2.01, 2.01, 2.01});
        pointsToTest.addTestPoint(new double[]{4.1, 4.9, 5.1}, new double[]{2.01, 2.01, 2.01});

        for (PointsToTest.TestPoint testPoint : pointsToTest.getTestPoints()) {
            double[] input = testPoint.getInput();
            double[] expectedOutput = testPoint.getExpectedOutput();
            try {
                double output = optimizer.optimize(function, GoalType.MINIMIZE, input[0], input[1], input[2]).getValue();
                Assert.assertArrayEquals(expectedOutput, new double[]{output}, 0.001);
            } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
                Assert.fail("Test failed: " + e.getMessage());
            }
        }
    }
}

class MyFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 4 * x + 4;
    }

    @Override
    public double derivative(double x) {
        return 2 * x - 4;
    }
}

class BrokenFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new RuntimeException("Function evaluation failed");
    }

    @Override
    public double derivative(double x) {
        return 0;
    }
}