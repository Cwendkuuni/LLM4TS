package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalTypeFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.MultivariateFunction;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testNullConstructor() {
        assertThrows(IllegalArgumentException.class, SimplexOptimizer::new);
    }

    @Test
    public void testDefaultConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(1.0e-6, 1.0e-6);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        new SimplexOptimizer().setSimplex(null);
    }

    @Test
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[2]));
    }

    @Test
    public void testOptimizeWithNoEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimize(0, f, goalType, new double[2]));
    }

    @Test
    public void testOptimizeWithEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        PointValuePair optimum = optimizer.optimize(100, f, goalType, startPoint);
        assertEquals(-1.0, optimum.getValue(), 1e-6);
        assertEquals(0.0, optimum.getPoint()[0], 1e-6);
        assertEquals(0.0, optimum.getPoint()[1], 1e-6);
    }

    @Test
    public void testOptimizeWithGoalTypeMismatch() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MAXIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimize(100, f, goalType, startPoint));
    }

    @Test
    public void testOptimizeWithData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter(AbstractSimplex.SIMPLEX_KEY, new SimpleSimplex(2));
        PointValuePair optimum = optimizer.optimize(100, f, goalType, optimizationData, startPoint);
        assertEquals(-1.0, optimum.getValue(), 1e-6);
        assertEquals(0.0, optimum.getPoint()[0], 1e-6);
        assertEquals(0.0, optimum.getPoint()[1], 1e-6);
        AbstractSimplex simplex = (AbstractSimplex) optimizationData.getParameter(AbstractSimplex.SIMPLEX_KEY);
        assertEquals(2, simplex.getSize());
    }

    @Test
    public void testOptimizeWithStartPointInData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter(AbstractSimplex.SIMPLEX_KEY, new SimpleSimplex(2));
        optimizationData.setParameter(AbstractSimplex.START_POINT_KEY, new double[]{1.0, 1.0});
        PointValuePair optimum = optimizer.optimize(100, f, goalType, optimizationData);
        assertEquals(-2.0, optimum.getValue(), 1e-6);
        assertEquals(1.0, optimum.getPoint()[0], 1e-6);
        assertEquals(1.0, optimum.getPoint()[1], 1e-6);
    }

    @Test
    public void testOptimizeWithFunctionMappingAdapter() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction mappingFunction = new MultivariateFunctionMappingAdapter() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair optimum = optimizer.optimize(100, mappingFunction, goalType, startPoint);
        assertEquals(-1.0, optimum.getValue(), 1e-6);
        assertEquals(0.0, optimum.getPoint()[0], 1e-6);
        assertEquals(0.0, optimum.getPoint()[1], 1e-6);
    }

    @Test
    public void testOptimizeWithPenaltyAdapter() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction penaltyFunction = new MultivariateFunctionPenaltyAdapter() {
            @Override
            public double value(double[] point) {
                double value = 0.0;
                for (int i = 0; i < point.length; i++) {
                    value += Math.pow(point[i] - 1.0, 2);
                }
                return value;
            }
        };
        MultivariateFunction objectiveFunction = (x) -> Math.pow(x[0], 2) + Math.pow(x[1], 2);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        PointValuePair optimum = optimizer.optimize(100, objectiveFunction, goalType, startPoint);
        assertEquals(3.0, optimum.getValue(), 1e-6);
        PointValuePair penalizedOptimum = optimizer.optimize(100, penaltyFunction, goalType, startPoint);
        assertEquals(0.0, penalizedOptimum.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, penalizedOptimum.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeWithDifferentNumberOfVariables() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimize(100, f, goalType, new double[3], startPoint));
    }

    @Test
    public void testOptimizeWithDifferentGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MAXIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimize(100, f, goalType, startPoint));
    }

    @Test
    public void testOptimizeWithDifferentEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = (x) -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{0.0, 0.0};
        assertThrows(IllegalArgumentException.class,
                () -> optimizer.optimize(5, f, goalType, startPoint));
    }

    @Test
    public void testOptimizeWithFunctionMismatch() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        GoalTypeFunction goalTypeFunction = new GoalTypeFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        };
        assertThrows(ClassCastException.class,
                () -> optimizer.optimize(100, goalTypeFunction, GoalType.MINIMIZE, new double[2]));
    }

    private static class SimpleSimplex implements AbstractSimplex {
        private final int size;

        public SimpleSimplex(int size) {
            this.size = size;
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public void build(double[] point) {
            // Empty implementation
        }

        @Override
        public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation
        }

        @Override
        public PointValuePair getPoint(int index) {
            double[] point = new double[size];
            point[index] = index;
            return new PointValuePair(point, index * index);
        }

        @Override
        public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation
        }
    }
}