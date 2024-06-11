package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleOptimizationData;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testEmpty() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.optimize(1, new DummyFunction(), GoalType.MINIMIZE, new Object[]{}));
    }

    @Test
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
        assertNull(optimizer.optimize(1, new DummyFunction(), GoalType.MINIMIZE, new Object[]{}));
    }

    @Test
    public void testOptimizeMinimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(createSimplex());

        PointValuePair result = optimizer.optimize(10, new DummyFunction(), GoalType.MINIMIZE, new Object[]{});
        assertNotNull(result);
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 0.001);

        optimizer = new SimplexOptimizer(0.001, 1e-10);
        result = optimizer.optimize(10, new DummyFunction(), GoalType.MINIMIZE, new Object[]{});
        assertNotNull(result);
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeMaximize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(createSimplex());

        PointValuePair result = optimizer.optimize(10, new DummyFunction(), GoalType.MAXIMIZE, new Object[]{});
        assertNotNull(result);
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 0.001);

        optimizer = new SimplexOptimizer(0.001, 1e-10);
        result = optimizer.optimize(10, new DummyFunction(), GoalType.MAXIMIZE, new Object[]{});
        assertNotNull(result);
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 0.001);
    }

    @Test
    public void testConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.01, 1e-6));
        optimizer.setSimplex(createSimplex());

        PointValuePair previous = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        optimizer.optimize(10, new DummyFunction(), GoalType.MINIMIZE, new Object[]{previous});

        double[] currentPoint = optimizer.getLastPoint();
        assertTrue(FunctionUtils.isCloseTo(currentPoint[0], previous.getPoint()[0], 0.01));
        assertTrue(FunctionUtils.isCloseTo(currentPoint[1], previous.getPoint()[1], 0.01));
        assertTrue(FunctionUtils.isCloseTo(optimizer.getLastValue(), previous.getValue(), 0.01));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, null, GoalType.MINIMIZE, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, new DummyFunction(), GoalType.SADDLE, new Object[]{});
    }

    private AbstractSimplex createSimplex() {
        return new SimpleSimplex(new InitialGuess() {
            public double[] createInitialGuess(int n) {
                double[] result = new double[n];
                result[0] = 0.5;
                result[1] = 0.5;
                return result;
            }
        });
    }

    private static class SimpleSimplex extends AbstractSimplex {
        private static final long serialVersionUID = 1L;

        public SimpleSimplex(InitialGuess initialGuess) {
            super(initialGuess);
        }

        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public void build(double[] initialPoint) {
            // Empty implementation
        }

        @Override
        public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation
        }

        @Override
        public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation
        }

        @Override
        public PointValuePair[] getPoints() {
            return new PointValuePair[] {
                new SimpleOptimizationData(getPoint(0), getValue(0)),
                new SimpleOptimizationData(getPoint(1), getValue(1))
            };
        }

        @Override
        public double[] getPoint(int index) {
            return new double[]{index == 0 ? 0.5 : 1.5, index == 0 ? 0.5 : 0.5};
        }

        @Override
        public double getValue(int index) {
            return index == 0 ? -1.0 : 1.0;
        }
    }

    private static class SimpleOptimizationData implements OptimizationData, PointValuePair {
        private static final long serialVersionUID = 1L;

        private final double[] point;
        private final double value;

        public SimpleOptimizationData(double[] point, double value) {
            this.point = point;
            this.value = value;
        }

        @Override
        public double[] getPoint() {
            return point;
        }

        @Override
        public double getValue() {
            return value;
        }
    }

    private static class DummyFunction implements MultivariateFunction {
        public double value(double[] x) {
            return 0.0;
        }
    }
}