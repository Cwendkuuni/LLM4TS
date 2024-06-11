package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.OptimizationData;

import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assert(optimizer.simplex != null);
        assert(optimizer.simplex.equals(simplex));
    }

    @Test(expected = NullPointerException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeWithSimpleValueChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[] { 1.0, 2.0 };
        MaxEval maxEval = new MaxEval(1000);
        OptimizationData data = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getPoint() {
                        return startPoint;
                    }
                };
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new SimpleSimplex(2);
            }
        };

        PointValuePair result = optimizer.optimize(maxEval, f, goalType, data);
        assertTrue(result.getValue() < 6.0);
        assertArrayEquals(startPoint, result.getPoint(), 1e-9);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[] { 1.0, 2.0 };
        MaxEval maxEval = new MaxEval(1000);
        OptimizationData data = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getPoint() {
                        return startPoint;
                    }
                };
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new SimpleSimplex(2);
            }
        };
        optimizer.optimize(maxEval, null, goalType, data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[] { 1.0, 2.0 };
        MaxEval maxEval = new MaxEval(1000);
        OptimizationData data = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return null;
            }
        };

        optimizer.optimize(maxEval, new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, goalType, data);
    }

    @Test
    public void testOptimizeCustomSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[] { 1.0, 2.0 };
        MaxEval maxEval = new MaxEval(1000);
        OptimizationData data = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getPoint() {
                        return startPoint;
                    }
                };
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new CustomSimplex(new double[][] {
                        {0.0, 0.0}, {1.0, 1.0}, {-1.0, 1.0}, {-1.0, -1.0}, {0.0, -1.0}
                });
            }
        };

        PointValuePair result = optimizer.optimize(maxEval, f, goalType, data);
        assertTrue(result.getValue() < 6.0);
        assertArrayEquals(startPoint, result.getPoint(), 1e-9);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new CustomValueChecker());
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[] { 1.0, 2.0 };
        MaxEval maxEval = new MaxEval(1000);
        OptimizationData data = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new InitialGuess() {
                    @Override
                    public double[] getPoint() {
                        return startPoint;
                    }
                };
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new SimpleSimplex(2);
            }
        };

        PointValuePair result = optimizer.optimize(maxEval, f, goalType, data);
        assertTrue(result.getValue() < 6.0);
        assertArrayEquals(startPoint, result.getPoint(), 1e-9);
    }
}

class SimpleSimplex implements AbstractSimplex {
    private final int dimension;
    private final double[][] vertices;

    public SimpleSimplex(int dimension) {
        this.dimension = dimension;
        this.vertices = new double[dimension + 1][dimension];
        for (int i = 0; i < dimension + 1; i++) {
            for (int j = 0; j < dimension; j++) {
                vertices[i][j] = (i == j) ? 0.0 : (i > j) ? 1.0 : -1.0;
            }
        }
    }

    @Override
    public int getSize() {
        return vertices.length;
    }

    @Override
    public double[] getPoint(int index) {
        return vertices[index];
    }

    @Override
    public void build(double[] initPoint) {
        // No-op
    }

    @Override
    public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
        PointValuePair[] results = new PointValuePair[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            results[i] = new PointValuePair(getPoint(i), f.value(getPoint(i)));
        }
        Arrays.sort(results, comparator);
    }

    @Override
    public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
        PointValuePair[] results = new PointValuePair[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            results[i] = new PointValuePair(getPoint(i), f.value(getPoint(i)));
        }

        int worstIndex = 0;
        for (int i = 1; i < vertices.length; i++) {
            if (results[i].getValue() < results[worstIndex].getValue()) {
                worstIndex = i;
            }
        }

        swap(vertices[worstIndex], vertices[0]);
        double[] worstPoint = vertices[0];
        vertices[0] = vertices[worstIndex];
        vertices[worstIndex] = worstPoint;
    }
}

class CustomSimplex extends SimpleSimplex {
    public CustomSimplex(double[][] vertices) {
        super(vertices[0].length);
        this.vertices = vertices;
    }
}

class CustomValueChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iteration, PointValuePair prev, PointValuePair current) {
        return prev.getValue() - current.getValue() < 0.001;
    }
}