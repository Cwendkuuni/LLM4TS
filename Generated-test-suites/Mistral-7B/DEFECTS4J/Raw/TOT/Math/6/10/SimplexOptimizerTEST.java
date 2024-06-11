package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.nonlinear.scalings.LevelSetScaling;
import org.apache.commons.math3.optimization.nonlinear.scalings.SasakiScaling;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** A simple function for testing. */
    private static final MultivariateFunction F1 = new MultivariateFunction() {
        public double value(double[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    };

    /** A more complex function for testing. */
    private static final MultivariateFunction F2 = new MultivariateFunction() {
        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < point.length; i++) {
                sum += Math.pow(point[i] - 2.0, 2.0);
            }
            return sum;
        }
    };

    /** A test simplex. */
    private static final AbstractSimplex TEST_SIMPLEX = new AbstractSimplex() {
        private static final long serialVersionUID = 1L;

        @Override
        public int getSize() {
            return 3;
        }

        @Override
        public double[] getStartPoint() {
            return new double[] { 0.1, 0.1, 0.1 };
        }

        @Override
        public void build(double[] startPoint) {
            // no-op
        }

        @Override
        public void evaluate(MultivariateFunction f, Comparator<? super PointValuePair> comparator) {
            // no-op
        }

        @Override
        public void iterate(MultivariateFunction f, Comparator<? super PointValuePair> comparator) {
            // no-op
        }

        @Override
        public PointValuePair[] getPoints() {
            return new PointValuePair[] {
                new PointValuePair(new double[] { 0.1, 0.1, 0.1 }, 1.0),
                new PointValuePair(new double[] { 0.2, 0.2, 0.2 }, 0.8),
                new PointValuePair(new double[] { 0.3, 0.3, 0.3 }, 0.4)
            };
        }
    };

    /** A test convergence checker. */
    private static final SimpleValueChecker TEST_CHECKER = new SimpleValueChecker(1e-6, 1e-6);

    /**
     * Tests the SimpleOptimizer with a simple test function.
     */
    @Test
    public void testSimpleFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        OptimizationData data = new OptimizationData() {
            private static final long serialVersionUID = 1L;

            @Override
            public AbstractSimplex getSimplex() {
                return TEST_SIMPLEX;
            }
        };
        PointValuePair result = optimizer.optimize(100, F1, GoalType.MINIMIZE, data);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Tests the SimpleOptimizer with a more complex test function.
     */
    @Test
    public void testComplexFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        OptimizationData data = new OptimizationData() {
            private static final long serialVersionUID = 1L;

            @Override
            public AbstractSimplex getSimplex() {
                return TEST_SIMPLEX;
            }
        };
        PointValuePair result = optimizer.optimize(100, F2, GoalType.MINIMIZE, data);
        assertNotNull(result);
        assertEquals(3.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 2.0, 2.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Tests the SimpleOptimizer with a simple test function and no simplex
     * provided.
     */
    @Test(expected = NullArgumentException.class)
    public void testSimpleFunctionNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        PointValuePair result = optimizer.optimize(100, F1, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a more complex test function and no simplex
     * provided.
     */
    @Test(expected = NullArgumentException.class)
    public void testComplexFunctionNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        PointValuePair result = optimizer.optimize(100, F2, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a simple test function and a wrong simplex
     * size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleFunctionWrongSize() {
        AbstractSimplex wrongSizeSimplex = new AbstractSimplex() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getSize() {
                return 2;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        OptimizationData data = new OptimizationData() {
            private static final long serialVersionUID = 1L;

            @Override
            public AbstractSimplex getSimplex() {
                return wrongSizeSimplex;
            }
        };
        PointValuePair result = optimizer.optimize(100, F1, GoalType.MINIMIZE, data);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a more complex test function and a wrong simplex
     * size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComplexFunctionWrongSize() {
        AbstractSimplex wrongSizeSimplex = new AbstractSimplex() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getSize() {
                return 2;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(TEST_CHECKER);
        OptimizationData data = new OptimizationData() {
            private static final long serialVersionUID = 1L;

            @Override
            public AbstractSimplex getSimplex() {
                return wrongSizeSimplex;
            }
        };
        PointValuePair result = optimizer.optimize(100, F2, GoalType.MINIMIZE, data);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a simple test function and a null convergence
     * checker.
     */
    @Test(expected = NullPointerException.class)
    public void testSimpleFunctionNullChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        PointValuePair result = optimizer.optimize(100, F1, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a more complex test function and a null convergence
     * checker.
     */
    @Test(expected = NullPointerException.class)
    public void testComplexFunctionNullChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        PointValuePair result = optimizer.optimize(100, F2, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    /**
     * Tests the SimpleOptimizer with a simple test function and a convergence checker
     * that doesn't converge.
     */
    @Test
    public void testSimpleFunctionDivergentChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-16, 1e-16));
        PointValuePair result = optimizer.optimize(100, F1, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertFalse(Double.isFinite(result.getValue()));
        assertNotNull(result.getPoint());
    }

    /**
     * Tests the SimpleOptimizer with a more complex test function and a convergence checker
     * that doesn't converge.
     */
    @Test
    public void testComplexFunctionDivergentChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-16, 1e-16));
        PointValuePair result = optimizer.optimize(100, F2, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertFalse(Double.isFinite(result.getValue()));
        assertNotNull(result.getPoint());
    }
}