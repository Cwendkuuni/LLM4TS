import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class BrentOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testDoOptimizeThrowException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new FunctionWithException("x^2", x -> {
            if (x < 0) {
                throw new FunctionEvaluationException("Function evaluation failed");
            }
            return x * x;
        });
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test
    public void testOptimizeMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5)[0];
        Assert.assertEquals(0.25, result, 1e-8);
    }

    @Test
    public void testOptimizeMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Function("x^2", x -> -x * x);
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, 0, 1, 0.5)[0];
        Assert.assertEquals(0.25, result, 1e-8);
    }

    @Test
    public void testOptimizeRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Function("x^3 - 2 * x + 1", x -> x * x * x - 2 * x + 1);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10)[0];
        Assert.assertEquals(-1, result, 1e-8);
    }

    @Test
    public void testOptimizeSmallRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 0.01, 0.5)[0];
        Assert.assertEquals(0.5, result, 1e-8);
    }

    @Test
    public void testOptimizeSmallAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1E-4);
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5)[0];
        Assert.assertEquals(0.24999999999999998, result, 1e-5);
    }

    @Test
    public void testOptimizeMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        UnivariateRealFunction function = new Function("x^2", x -> x * x);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    private static UnivariateRealFunction newFunction(String name, FunctionEvaluator evaluator) {
        return x -> {
            try {
                return evaluator.evaluate(x);
            } catch (Exception e) {
                throw new FunctionEvaluationException(name, e);
            }
        };
    }

    private static UnivariateRealFunction newFunction(String name, DoubleFunction function) {
        return x -> function.value(x);
    }

    private interface FunctionEvaluator {
        double evaluate(double x) throws Exception;
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final FunctionEvaluator evaluator;

        public Function(String name, FunctionEvaluator evaluator) {
            this.name = name;
            this.evaluator = evaluator;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            try {
                return evaluator.evaluate(x);
            } catch (Exception e) {
                throw new FunctionEvaluationException(name, e);
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static interface DoubleFunction {
        double value(double x);
    }

    private static class FunctionWithException implements UnivariateRealFunction {
        private final String name;
        private final FunctionEvaluator evaluator;

        public FunctionWithException(String name, FunctionEvaluator evaluator) {
            this.name = name;
            this.evaluator = evaluator;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            try {
                return evaluator.evaluate(x);
            } catch (Exception e) {
                throw new FunctionEvaluationException(name, e);
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @FunctionalInterface
    private interface FunctionEvaluator {
        double evaluate(double x) throws FunctionEvaluationException, Exception;
    }

    @FunctionalInterface
    private interface DoubleFunction {
        double value(double x);
    }

    private static class RandomNumberGenerator {
        private final Random random;

        public RandomNumberGenerator() {
            this.random = new Random();
        }

        public double nextDouble(double lowerBound, double upperBound) {
            return lowerBound + random.nextDouble() * (upperBound - lowerBound);
        }
    }

}