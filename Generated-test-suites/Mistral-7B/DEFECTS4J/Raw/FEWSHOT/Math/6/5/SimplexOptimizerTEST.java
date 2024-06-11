package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.junit.Test;

public class SimplexOptimizerTest {

    private AbstractSimplex simplex;
    private MultivariateFunction function;

    @Test
    public void testSetSimplex() {
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(null));
        simplex = new MockSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeSimpleFunction() {
        simplex = new MockSimplex();
        function = x -> FunctionUtils.sum(x);

        PointValuePair result = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(10, function, GoalType.MINIMIZE, new OptimizationData[]{simplex});
        double[] expected = new double[]{0.0};
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(expected, result.getPoint(), 1.0e-6);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testOptimizeComplexFunction() {
        simplex = new MockSimplex();
        function = x -> Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2) + Math.exp(-Math.pow(x[0], 2) - Math.pow(x[1], 2));

        PointValuePair result = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(10, function, GoalType.MINIMIZE, new OptimizationData[]{simplex});
        double[] expected = new double[]{Math.PI / 2, Math.PI / 4};
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(expected, result.getPoint(), 1.0e-3);
        assertEquals(1.2646633423381491e-06, result.getValue(), 1.0e-6);
    }

    @Test
    public void testOptimizeWithNullSimplex() {
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(null).optimize(1, new MockFunction(), GoalType.MINIMIZE, new OptimizationData[0]));
    }

    @Test
    public void testOptimizeWithNullFunction() {
        simplex = new MockSimplex();
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(1, null, GoalType.MINIMIZE, new OptimizationData[]{simplex}));
    }

    @Test
    public void testOptimizeWithNullData() {
        simplex = new MockSimplex();
        function = x -> FunctionUtils.sum(x);
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(1, function, GoalType.MINIMIZE, null));
    }

    @Test
    public void testOptimizeWithEmptyData() {
        simplex = new MockSimplex();
        function = x -> FunctionUtils.sum(x);
        assertThrows(IllegalArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(1, function, GoalType.MINIMIZE, new OptimizationData[0]));
    }

    @Test
    public void testOptimizeWithoutSimplex() {
        function = x -> FunctionUtils.sum(x);
        assertThrows(IllegalArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6)).optimize(1, function, GoalType.MINIMIZE, new OptimizationData[0]));
    }

    private static class MockFunction implements MultivariateFunction {
        public double value(double[] point) {
            return 0.0;
        }
    }

    private static class MockSimplex implements AbstractSimplex {
        public int getSize() {
            return 2;
        }

        public double[] getPoint(int index) {
            return new double[]{index / 2.0, index % 2.0};
        }

        public PointValuePair[] getPoints() {
            return new PointValuePair[]{
                new PointValuePair(new double[]{0.0, 0.0}, 1.0),
                new PointValuePair(new double[]{1.0, 0.0}, 2.0),
                new PointValuePair(new double[]{0.5, 0.5}, 1.5)
            };
        }

        public void build(double[] point) {
            // Empty
        }

        public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
            // Empty
        }

        public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
            // Empty
        }
    }
}