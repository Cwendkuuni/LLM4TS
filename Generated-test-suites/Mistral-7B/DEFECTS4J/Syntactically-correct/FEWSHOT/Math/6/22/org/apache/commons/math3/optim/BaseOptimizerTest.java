package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergence.ConvergenceChecker;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testBaseOptimizerGetters() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
        optimizer = new DummyBaseOptimizer<>(100);
        assertEquals(100, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(new DummyUnivariateFunction(), new SimpleBounds(0.0, 1.0));
        assertEquals(1, optimizer.getEvaluations());

        ConvergenceChecker<PAIR> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer = new DummyBaseOptimizer<>(convergenceChecker);
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testBaseOptimizerGettersNull() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getMaxEvaluations());
        assertNull(optimizer.getEvaluations());
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer<>(null);
        assertNull(optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>(new DummyConvergenceChecker<>(null));
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBaseOptimizerMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        try {
            optimizer.setMaxEvaluations(-1);
            fail("Maximum number of evaluations cannot be negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        optimizer = new DummyBaseOptimizer<>(new DummyUnivariateOptimizer<>());
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>();
        optimizer.setMaxEvaluations(MaxEval.UNLIMITED);
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
    }

    @Test
    public void testBaseOptimizerEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        UnivariateOptimizer<Double, UnivariateFunction> univariateOptimizer = new DummyUnivariateOptimizer<>();
        optimizer = new DummyBaseOptimizer<>(univariateOptimizer);
        optimizer.optimize(new DummyUnivariateFunction(), new SimpleBounds(0.0, 1.0));
        assertEquals(1, optimizer.getEvaluations());

        optimizer = new DummyBaseOptimizer<>();
        optimizer.optimize(new DummyUnivariateFunction(), new SimpleBounds(0.0, 1.0));
        optimizer.optimize(new DummyUnivariateFunction(), new SimpleBounds(0.0, 1.0));
        assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testBaseOptimizerConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        ConvergenceChecker<PAIR> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer = new DummyBaseOptimizer<>(convergenceChecker);
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer<>();
        try {
            optimizer.setConvergenceChecker(null);
            fail("Convergence checker cannot be null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;

        public DummyBaseOptimizer() {
            this(null);
        }

        public DummyBaseOptimizer(int maxEvaluations) {
            this(maxEvaluations, null);
        }

        public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
            this(MaxEval.UNLIMITED, convergenceChecker);
        }

        public DummyBaseOptimizer(int maxEvaluations, ConvergenceChecker<PAIR> convergenceChecker) {
            this.maxEvaluations = maxEvaluations;
            this.evaluations = 0;
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        public void optimize(UnivariateFunction function, SimpleBounds bounds) {
            evaluations++;
        }
    }

    private static class DummyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
        private ConvergenceChecker<Object> delegate;

        public DummyConvergenceChecker() {
            this(null);
        }

        public DummyConvergenceChecker(ConvergenceChecker<Object> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean converged(PAIR currentValue, PAIR bestValue, int iteration) {
            if (delegate != null) {
                return delegate.converged(null, null, iteration);
            }
            return true;
        }

        @Override
        public boolean isConverged(PAIR currentValue, PAIR bestValue) {
            if (delegate != null) {
                return delegate.isConverged(null, bestValue);
            }
            return true;
        }

        @Override
        public void reset() {
            if (delegate != null) {
                delegate.reset();
            }
        }
    }

    private static class DummyUnivariateFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public int numberOfVariables() {
            return 1;
        }

        @Override
        public void gradient(double x, double[] grad, int start, int end) {
            if (grad != null) {
                grad[0] = 2 * x;
            }
        }

        @Override
        public void valueGradient(double x, double[] grad) {
            gradient(x, grad, 0, grad.length);
        }

        @Override
        public void valueHessian(double x, double[][] hess) {
            if (hess != null) {
                hess[0][0] = 2;
            }
        }
    }
}