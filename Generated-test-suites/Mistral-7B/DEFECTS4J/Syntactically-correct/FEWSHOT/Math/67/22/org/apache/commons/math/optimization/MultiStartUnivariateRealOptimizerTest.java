package org.apache.commons.cli.optimization; //assuming the package name is correct in your code

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private final UnivariateRealFunction function;
    private final GoalType goalType;
    private final double min;
    private final double max;
    private final int maxIterations;
    private final int maxEvaluations;
    private final int starts;
    private final RandomGenerator generator;

    public MultiStartUnivariateRealOptimizerTest(UnivariateRealFunction function, GoalType goalType, double min, double max, int maxIterations, int maxEvaluations, int starts, RandomGenerator generator) {
        this.function = function;
        this.goalType = goalType;
        this.min = min;
        this.max = max;
        this.maxIterations = maxIterations;
        this.maxEvaluations = maxEvaluations;
        this.starts = starts;
        this.generator = generator;
    }

    @Parameterized.Parameters(name = "{index}: Testing MultiStartUnivariateRealOptimizer with function={0}, goalType={1}, min={2}, max={3}, maxIterations={4}, maxEvaluations={5}, starts={6} and generator={7}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new QuadraticFunction(1.0), GoalType.MINIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 10, new FakeRandomGenerator(),
                new QuadraticFunction(1.0), GoalType.MAXIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 10, new FakeRandomGenerator(),
                new QuadraticFunction(-1.0), GoalType.MINIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 5, new FakeRandomGenerator(),
                new QuadraticFunction(-1.0), GoalType.MAXIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 5, new FakeRandomGenerator(),
                new QuadraticFunction(0.0), GoalType.MINIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 1, new FakeRandomGenerator(),
                new QuadraticFunction(0.0), GoalType.MAXIMIZE, -10.0, 10.0, 100, Integer.MAX_VALUE, 1, new FakeRandomGenerator()
        });
    }

    @Test
    public void testOptimize() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function), starts, generator);
        optimizer.setMaxIterations(maxIterations);
        optimizer.setMaxEvaluations(maxEvaluations);

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        double result = optimizer.optimize(function, goalType, min, max);

        assertEquals(starts + 1, optima.length);
        assertEquals(starts + 1, optimaValues.length);
        assertArrayEquals(new double[]{result}, Arrays.copyOfRange(optima, 0, 1));
        assertArrayEquals(new double[]{result}, Arrays.copyOfRange(optimaValues, 0, 1));
        for (int i = 1; i < optima.length; i++) {
            assertNotEquals(result, optima[i]);
            assertNotEquals(result, optimaValues[i]);
        }
        for (int i = 0; i < optima.length; i++) {
            assertTrue(Double.isNaN(optima[i]) || Double.isFinite(optima[i]));
            assertTrue(Double.isNaN(optimaValues[i]) || Double.isFinite(optimaValues[i]));
        }

        // Assert some statistics
        assertEquals(totalIterations, optimizer.getIterationCount());
        assertEquals(totalEvaluations, optimizer.getEvaluations());
    }

    private static class FakeRandomGenerator implements RandomGenerator {
        @Override
        public double nextDouble() {
            return 0.5;
        }
    }

    private static class QuadraticFunction implements UnivariateRealFunction {
        private final double a;

        public QuadraticFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * a * x;
        }
    }

}

class SimpleUnivariateRealOptimizer implements UnivariateRealOptimizer {
    private final UnivariateRealFunction function;

    public SimpleUnivariateRealOptimizer(UnivariateRealFunction function) {
        this.function = function;
    }

    @Override
    public double getFunctionValue() {
        return function.value(0.0);
    }

    @Override
    public double getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAbsoluteAccuracy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIterationCount() {
        return 0;
    }

    @Override
    public int getMaxIterations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxIterations(int maxIterations) {
        // no-op
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public int getMaxEvaluations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        // no-op
    }

    @Override
    public double getRelativeAccuracy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetAbsoluteAccuracy() {
        // no-op
    }

    @Override
    public void resetMaxIterations() {
        // no-op
    }

    @Override
    public void resetRelativeAccuracy() {
        // no-op
    }

    @Override
    public void setAbsoluteAccuracy(double accuracy) {
        // no-op
    }

    @Override
    public void setMaxIterations(int maxIterations) {
        // no-op
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        // no-op
    }

    @Override
    public void setRelativeAccuracy(double accuracy) {
        // no-op
    }

    @Override
    public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
            throws ConvergenceException, FunctionEvaluationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
            throws ConvergenceException, FunctionEvaluationException {
        throw new UnsupportedOperationException();
    }
}