package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.multivariate.MultivariateFunction;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testParseOptimizationData() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        OptimizationData[] optData = {
                new OptimizationData() {
                    @Override
                    public Object getParameterByName(String name) {
                        if ("function".equals(name)) {
                            return f;
                        }
                        return null;
                    }
                },
                new OptimizationData() {
                    @Override
                    public Object getParameterByName(String name) {
                        if ("simplex".equals(name)) {
                            AbstractSimplex simplex = new SimpleSimplex(2);
                            simplex.setMaxIterations(100);
                            return simplex;
                        }
                        return null;
                    }
                }
        };
        optimizer.parseOptimizationData(optData);
        assertTrue(optimizer.getSimplex() != null);
        assertFalse(optimizer.getInitialGuess() != null);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assertTrue(optimizer.getSimplex() == simplex);
    }

    @Test(expected = NullPointerException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimize() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        AbstractSimplex simplex = new SimpleSimplex(2);
        simplex.setMaxIterations(100);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(10000, f, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        double[] point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point, 1e-5);
        double value = result.getValue();
        assertTrue(value < 1e-5);
    }

    @Test
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AssertionError error = assertThrows(NullArgumentException.class, () -> optimizer.optimize(10000, null, GoalType.MINIMIZE, new double[]{1.0, 1.0}));
        assertTrue(error.getMessage().contains("function"));
    }

    @Test
    public void testOptimizeNoInitialGuess() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(10000, f, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeNoSimplex() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        PointValuePair result = optimizer.optimize(10000, f, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        assertNull(result);
    }

    @Test
    public void testOptimizeNoIterations() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(0, f, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        assertNull(result);
    }

    @Test
    public void testOptimizeNoGoalType() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(10000, f, null, new double[]{1.0, 1.0});
        assertNull(result);
    }

    @Test
    public void testOptimizeNoInitialGuessNoSimplex() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        PointValuePair result = optimizer.optimize(10000, f, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeNoMaxIterations() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(Math.pow(x[0], 2) + Math.pow(x[1], 2)) - 1;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-3));
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(1, f, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        assertNull(result);
    }
}