package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonUnaryOptimizer;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with a simple function and a small number of restarts.
     * <p>
     * The test function is a quadratic polynomial with a global minimum at x = -1.
     * The optimizer is expected to find the minimum with a high probability.
     * </p>
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundMinMaxOptimizer(new PolynomialFunction(0.0, 1.0, 2.0)), 10,
                new UniformRandomGenerator());

        double min = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.0, min, 1e-9);

        // check that all starts converged
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        for (int i = 0; i < optima.length; ++i) {
            assertEquals(-1.0, optima[i], 1e-9);
        }

        // check that all starts converged to the minimum
        for (int i = 0; i < optimaValues.length; ++i) {
            assertEquals(-1.0, optimaValues[i], 1e-9);
        }
    }

    /**
     * Test optimization with a noisy function and a large number of restarts.
     * <p>
     * The test function is a noisy version of a quadratic polynomial with a global
     * minimum at x = -1. The optimizer is expected to find the minimum with a
     * high probability despite the noise.
     * </p>
     */
    @Test
    public void testNoisyFunction() {
        UnivariateRealFunction f = new NoisyPolynomialFunction(-1.0, 2.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundMinMaxOptimizer(new PolynomialFunction(0.0, 1.0, 2.0)), 100,
                new UniformRandomGenerator());

        double min = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.0, min, 1e-6);

        // check that all starts converged
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        for (int i = 0; i < optima.length; ++i) {
            assertEquals(-1.0, optima[i], 1e-6);
        }

        // check that all starts converged to the minimum
        for (int i = 0; i < optimaValues.length; ++i) {
            assertEquals(-1.0, optimaValues[i], 1e-6);
        }
    }

    /**
     * Test optimization with a function that has no solution.
     * <p>
     * The test function is a constant function with a value of 1. The optimizer
     * is expected to throw a ConvergenceException.
     * </p>
     */
    @Test(expected = ConvergenceException.class)
    public void testNoSolutionFunction() {
        UnivariateRealFunction f = new ConstFunction(1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundMinMaxOptimizer(new PolynomialFunction(0.0, 1.0, 2.0)), 10,
                new UniformRandomGenerator());

        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        fail("A ConvergenceException should have been thrown.");
    }

    /**
     * Test optimization with an optimizer that throws an exception.
     * <p>
     * The test function is a simple polynomial function and the optimizer is
     * a {@link FunctionEvaluationException} throwing optimizer. The optimizer
     * is expected to propagate the exception.
     * </p>
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testExceptionThrowingOptimizer() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new ExceptionThrowingOptimizer(), 10,
                new UniformRandomGenerator());

        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        fail("A FunctionEvaluationException should have been thrown.");
    }

    /**
     * Helper class for testing optimization with a simple bound optimizer.
     */
    private static class SimpleBoundMinMaxOptimizer
            implements NonUnaryOptimizer<UnivariateRealFunction, UnivariateRealOptimizer> {

        private final UnivariateRealOptimizer optimizer;

        public SimpleBoundMinMaxOptimizer(UnivariateRealOptimizer optimizer) {
            this.optimizer = optimizer;
        }

        @Override
        public double optimize(UnivariateRealFunction func, GoalType goalType, double start, double end)
                throws ConvergenceException, FunctionEvaluationException {
            return optimizer.optimize(new SimpleBounds(start, end), goalType, func);
        }

        @Override
        public double value(UnivariateRealFunction function, double x) {
            return function.value(x);
        }

    }

    /**
     * Helper function representing a constant function.
     */
    private static class ConstFunction implements UnivariateRealFunction {

        private final double constant;

        public ConstFunction(double constant) {
            this.constant = constant;
        }

        @Override
        public double value(double x) {
            return constant;
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }

    }

    /**
     * Helper class for throwing a FunctionEvaluationException.
     */
    private static class ExceptionThrowingOptimizer
            implements UnivariateRealOptimizer {

        @Override
        public double optimize(UnivariateRealFunction f, GoalType goalType, double start, double end)
                throws FunctionEvaluationException {
            throw new FunctionEvaluationException(LocalizedFormats.SOLVE_FAILED, "Failed to evaluate function");
        }

        @Override
        public double value(UnivariateRealFunction function, double x) {
            throw new UnsupportedOperationException("This optimizer does not support function evaluation.");
        }

        @Override
        public int getIterationCount() {
            throw new UnsupportedOperationException("This optimizer does not support iteration count.");
        }

        @Override
        public int getMaxIterations() {
            throw new UnsupportedOperationException("This optimizer does not support maximum iterations.");
        }

        @Override
        public int getMaxEvaluations() {
            throw new UnsupportedOperationException("This optimizer does not support maximum evaluations.");
        }

        @Override
        public int getEvaluations() {
            throw new UnsupportedOperationException("This optimizer does not support evaluations.");
        }

        @Override
        public double getAbsoluteAccuracy() {
            throw new UnsupportedOperationException("This optimizer does not support absolute accuracy.");
        }

        @Override
        public double getRelativeAccuracy() {
            throw new UnsupportedOperationException("This optimizer does not support relative accuracy.");
        }

        @Override
        public void setAbsoluteAccuracy(double accuracy) {
            // empty
        }

        @Override
        public void setMaxIterations(int iterations) {
            // empty
        }

        @Override
        public void setMaxEvaluations(int evaluations) {
            // empty
        }

        @Override
        public void setRelativeAccuracy(double accuracy) {
            // empty
        }

        @Override
        public boolean hasConverged() {
            throw new UnsupportedOperationException("This optimizer does not support convergence.");
        }

        @Override
        public void resetAbsoluteAccuracy() {
            // empty
        }

        @Override
        public void resetMaxIterations() {
            // empty
        }

        @Override
        public void resetRelativeAccuracy() {
            // empty
        }

        @Override
        public double getIncrement() {
            throw new UnsupportedOperationException("This optimizer does not support increment.");
        }

        @Override
        public void setIncrement(double increment) {
            throw new UnsupportedOperationException("This optimizer does not support increment.");
        }

        @Override
        public MaxEval getMaxEval() {
            throw new UnsupportedOperationException("This optimizer does not support maximum evaluations.");
        }

        @Override
        public void setMaxEval(MaxEval maxEval) {
            throw new UnsupportedOperationException("This optimizer does not support maximum evaluations.");
        }

        @Override
        public void setMinimizing(boolean min) {
            // empty
        }

        @Override
        public void setTestingOptimum(boolean testing) {
            // empty
        }

        @Override
        public void setStoppingCondition(double value) {
            // empty
        }

    }

    /**
     * Helper function for generating random numbers.
     */
    private static class UniformRandomGenerator implements RandomGenerator {

        @Override
        public double nextGaussian() {
            return StatUtils.gmean(Math.random(), Math.random());
        }

        @Override
        public double nextDouble() {
            return Math.random();
        }

        @Override
        public double nextExponential(double lamda) {
            return -Math.log(1.0 - Math.random()) / lamda;
        }

        @Override
        public long nextLong() {
            return (long) (Math.random() * Double.MAX_VALUE);
        }

        @Override
        public int nextInt(int n) {
            return (int) (Math.random() * (n + 1));
        }

    }

    /**
     * Helper function for generating a noisy version of a polynomial.
     */
    private static class NoisyPolynomialFunction implements UnivariateRealFunction {

        private final double a;
        private final double b;
        private final double c;
        private final double noise;

        public NoisyPolynomialFunction(double a, double b, double c, double noise) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.noise = noise;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2) + b * x + c + noise * (Math.random() - 0.5);
        }

        @Override
        public double derivative(double x) {
            return 2.0 * a * x + b;
        }

    }

}