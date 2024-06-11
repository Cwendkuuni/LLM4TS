package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.AbstractSimplex;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimplexOptimizerTest {

    @org.junit.Test
    public void testConstructorWithDefaultConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @org.junit.Test
    public void testConstructorWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new SimpleValueChecker(0.001, 1.0);
        SimplexOptimizer optimizer = new SimplexOptimizer(customChecker);
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    @org.junit.Test
    public void testConstructorWithRelativeAndAbsoluteThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1e-6, 1e-8);
        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleValueChecker);
        SimpleValueChecker simpleChecker = (SimpleValueChecker) checker;
        assertEquals(1e-6, simpleChecker.getRelativeThreshold(), 1e-9);
        assertEquals(1e-8, simpleChecker.getAbsoluteThreshold(), 1e-9);
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testSetSimplexWithNullArg() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @org.junit.Test
    public void testOptimizeWithSimpleFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        Double[] startPoint = {1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData() {
            public Object getParameter(String name) {
                if (name.equals("InitialGuess")) {
                    return startPoint;
                }
                if (name.equals("AbstractSimplex")) {
                    return new SimpleSimplex(2, 3);
                }
                return null;
            }
        };
        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @org.junit.Test
    public void testOptimizeWithFunctionThrowingException() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) throws Exception {
                throw new Exception("Function exception");
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.optimize(1, function, GoalType.MINIMIZE, new OptimizationData[0]);
            fail("Expected an exception");
        } catch (Exception e) {
            assertEquals("Function exception", e.getMessage());
        }
    }

    @org.junit.Test
    public void testOptimizeWithDifferentNumberOfParameters() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        Double[] startPoint = {1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData() {
            public Object getParameter(String name) {
                if (name.equals("InitialGuess")) {
                    return startPoint;
                }
                if (name.equals("AbstractSimplex")) {
                    return new SimpleSimplex(1, 2);
                }
                return null;
            }
        };
        try {
            optimizer.optimize(1, function, goalType, optimizationData);
            fail("Expected an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Simplex must have the same dimensions as the objective function", e.getMessage());
        }
    }

    private static class SimpleSimplex implements AbstractSimplex {
        private int dimension;
        private int size;

        public SimpleSimplex(int dimension, int size) {
            this.dimension = dimension;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void build(double[] initialPoint) {
            // Empty implementation
        }

        public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
            // Empty implementation
        }

        public double[] getPoint(int index) {
            double[] point = new double[dimension];
            Arrays.fill(point, index / (double) size);
            return point;
        }

        public PointValuePair getWorstPoint() {
            double worstValue = Double.POSITIVE_INFINITY;
            PointValuePair worstPoint = null;
            for (int i = 0; i < size; i++) {
                double value = computeObjectiveValue(getPoint(i));
                if (value < worstValue) {
                    worstValue = value;
                    worstPoint = new PointValuePair(getPoint(i), worstValue);
                }
            }
            return worstPoint;
        }
    }
}