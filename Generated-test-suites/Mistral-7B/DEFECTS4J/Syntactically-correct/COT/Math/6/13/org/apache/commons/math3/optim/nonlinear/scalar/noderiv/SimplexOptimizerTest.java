package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.AbstractSimplex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for SimplexOptimizer class.
 */
public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-8;
    private AbstractSimplex simplex;
    private MultivariateFunction function;

    @Before
    public void setup() {
        simplex = new NelderMeadSimplex(3, new SimpleValueChecker(EPSILON));
        function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.sin(x[1]) * Math.sin(x[2]) - 1, 2);
            }
        };
    }

    @Test
    public void testConstructorWithDefaultChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithSimpleValueChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() > current.getValue();
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(customChecker);
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0001);
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeWithDefaultData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[3]);
        Assert.assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, new double[3]);
    }

    @Test
    public void testOptimizeMinimization() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[3]);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getValue() < 0);
    }

    @Test
    public void testOptimizeMaximization() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[3]);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getValue() > 0);
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        double[] initialGuess = new double[]{1.0, 1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE,
                new OptimizationData() {
                    public Object getParameterValue(String name) {
                        if (name.equals("InitialGuess")) {
                            return initialGuess;
                        }
                        return null;
                    }
                });
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(initialGuess, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeWithSimplexData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        OptimizationData optimizationData = new OptimizationData() {
            public Object getParameterValue(String name) {
                if (name.equals("AbstractSimplex")) {
                    return simplex;
                }
                return null;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, optimizationData);
        Assert.assertNotNull(result);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeWithMaxEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer() {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[3], 10);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNegativeMaxEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        optimizer.optimize(function, GoalType.MINIMIZE, new double[3], -1);
    }

    @Test
    public void testConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer() {
            @Override
            protected PointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setSimplex(simplex);
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() - current.getValue() <= EPSILON;
            }
        };
        optimizer.setConvergenceChecker(customChecker);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[3]);
        Assert.assertNotNull(result);
    }
}