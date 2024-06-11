package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) - 1;
            }
        };

        PointValuePair expected = new PointValuePair(new double[]{0.5, 0.5}, 0);

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);

        AbstractSimplex simplex = new SimpleSimplex(2);

        OptimizationData[] optimizationData = {new OptimizationData() {
            @Override
            public Object getParameterName(int i) {
                return "x" + (i + 1);
            }

            @Override
            public double[] getStartPoint() {
                return new double[]{1.0, 1.0};
            }

            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getPoint() {
                        return new double[]{1.0, 1.0};
                    }
                };
            }

            @Override
            public SimpleBounds[] getBounds() {
                return new SimpleBounds[]{new SimpleBounds(0.0, 2.0), new SimpleBounds(0.0, 2.0)};
            }
        }};

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);
        assertTrue(FunctionUtils.EQUALS_EPSILON.equals(expected.getValue(), result.getValue(), 1e-6));
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-6);
    }

    @Test
    public void testNullFunction() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNull(optimizer.optimize(1, null, GoalType.MINIMIZE, new OptimizationData[0]));
    }

    @Test
    public void testNullGoalType() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) - 1;
            }
        };

        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNull(optimizer.optimize(100, function, null, new OptimizationData[0]));
    }

    @Test
    public void testNullOptimizationData() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) - 1;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);

        assertNull(new SimplexOptimizer(checker).optimize(100, function, goalType, null));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) - 1;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);

        AbstractSimplex simplex = null;

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.setSimplex(simplex);
        optimizer.optimize(100, function, goalType, new OptimizationData[0]);
    }

    @Test
    public void testEmptyOptimizationData() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.sum(point) - 1;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.optimize(100, function, goalType, new OptimizationData[0]);
    }
}