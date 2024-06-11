package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testSimplexOptimizer() {
        // Test the constructor with default settings
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();

        // Test the constructor with a SimpleValueChecker
        SimpleValueChecker checker = new SimpleValueChecker(1e-5, 1e-5);
        SimplexOptimizer simplexOptimizerWithChecker = new SimplexOptimizer(checker);

        // Test the constructor with a custom relative and absolute tolerance
        SimpleValueChecker customChecker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer simplexOptimizerWithCustomChecker = new SimplexOptimizer(customChecker);

        // Test the constructor with a custom tolerance array
        double[] toleranceArray = new double[]{1e-7, 1e-7};
        SimpleValueChecker customToleranceChecker = new SimpleValueChecker(toleranceArray);
        SimplexOptimizer simplexOptimizerWithCustomTolerance = new SimplexOptimizer(customToleranceChecker);

        // Test the methods
        MultivariateFunction testFunction = new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0});

        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = simplexOptimizer.optimize(100, testFunction, goalType, startPoint);
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0, 1.0}, FunctionUtils.value(testFunction, startPoint));

        assertEquals(expectedResult, result);

        // Test with custom simplex
        SimpleXOptimizer simplex = new SimpleXOptimizer(customToleranceChecker);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        simplexOptimizerWithCustomTolerance.optimize(100, testFunction, goalType, optimizationData);
    }

    @Test
    public void testNullArguments() {
        try {
            SimplexOptimizer simplexOptimizer = null;
            simplexOptimizer.optimize(100, null, GoalType.MINIMIZE, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            MultivariateFunction testFunction = null;
            SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
            simplexOptimizer.optimize(100, testFunction, GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            double[] startPoint = null;
            SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
            simplexOptimizer.optimize(100, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), GoalType.MINIMIZE, startPoint);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyOptimizationData() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        try {
            simplexOptimizer.optimize(100, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), GoalType.MINIMIZE, optimizationData);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptySimplex() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.setSimplex(new AbstractSimplex(){
                @Override
                public void build(double[] initialPoint) {
                    // Empty implementation
                }

                @Override
                public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
                    // Empty implementation
                }

                @Override
                public PointValuePair[] getPoints() {
                    return new PointValuePair[0];
                }

                @Override
                public int getSize() {
                    return 0;
                }
            });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyInitialPoint() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.optimize(100, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), GoalType.MINIMIZE, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyFunction() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.optimize(100, null, GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyToleranceArray() {
        SimpleValueChecker customToleranceChecker = new SimpleValueChecker(new double[0]);
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer(customToleranceChecker);
        try {
            simplexOptimizer.optimize(100, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeMaxEval() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.optimize(-1, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullGoalType() {
        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.optimize(100, new PolynomialFunction(new double[]{-6.0, 3.0, 2.0}, 3, 1.0,
                    new double[]{-3.0, 2.0, 1.0}, new double[]{3.0, -2.0, 1.0}), null, new double[]{1.0, 1.0, 1.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeWithFunctionThatThrowsException() {
        class ExceptionFunction implements MultivariateFunction {
            @Override
            public double value(double[] point) {
                throw new RuntimeException("Function threw an exception");
            }
        }

        SimplexOptimizer simplexOptimizer = new SimplexOptimizer();
        try {
            simplexOptimizer.optimize(100, new ExceptionFunction(), GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        SimplexOptimizer optimizer1 = new SimplexOptimizer();
        SimplexOptimizer optimizer2 = new SimplexOptimizer();
        assertTrue(optimizer1.equals(optimizer2));

        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizer3 = new SimplexOptimizer(checker);
        assertFalse(optimizer1.equals(optimizer3));

        SimpleValueChecker customChecker = new SimpleValueChecker(1e-5, 1e-5);
        SimplexOptimizer optimizer4 = new SimplexOptimizer(customChecker);
        assertFalse(optimizer1.equals(optimizer4));

        SimpleXOptimizer simplex = new SimpleXOptimizer(1e-5, 1e-5);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        SimplexOptimizer optimizer5 = new SimplexOptimizer();
        optimizationData.setParameter("AbstractSimplex", simplex);
        assertFalse(optimizer1.equals(optimizer5));

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", new SimpleXOptimizer());
        assertFalse(optimizer1.equals(optimizer5));

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        assertFalse(optimizer1.equals(optimizationData));

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        optimizationData.setParameter("AbstractSimplex", simplex);
        assertTrue(optimizer1.equals(optimizationData));

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        optimizationData.setParameter("AbstractSimplex", new SimpleXOptimizer(1e-5, 1e-5));
        assertFalse(optimizer1.equals(optimizationData));
    }

    @Test
    public void testHashCode() {
        SimplexOptimizer optimizer1 = new SimplexOptimizer();
        SimplexOptimizer optimizer2 = new SimplexOptimizer();
        assertEquals(optimizer1.hashCode(), optimizer2.hashCode());

        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizer3 = new SimplexOptimizer(checker);
        assertNotEquals(optimizer1.hashCode(), optimizer3.hashCode());

        SimpleValueChecker customChecker = new SimpleValueChecker(1e-5, 1e-5);
        SimplexOptimizer optimizer4 = new SimplexOptimizer(customChecker);
        assertNotEquals(optimizer1.hashCode(), optimizer4.hashCode());

        SimpleXOptimizer simplex = new SimpleXOptimizer(1e-5, 1e-5);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        SimplexOptimizer optimizer5 = new SimplexOptimizer();
        optimizationData.setParameter("AbstractSimplex", simplex);
        assertNotEquals(optimizer1.hashCode(), optimizer5.hashCode());

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        optimizationData.setParameter("AbstractSimplex", simplex);
        assertEquals(optimizer1.hashCode(), optimizationData.hashCode());

        optimizationData = new OptimizationData();
        optimizationData.setParameter("AbstractSimplex", simplex);
        optimizationData.setParameter("AbstractSimplex", new SimpleXOptimizer(1e-5, 1e-5));
        assertNotEquals(optimizer1.hashCode(), optimizationData.hashCode());
    }

    private void assertEquals(PointValuePair expected, PointValuePair actual) {
        assertArrayEquals(expected.getPoint(), actual.getPoint(), 0.0);
        assertEquals(expected.getValue(), actual.getValue(), 0.0);
    }

    private void assertEquals(double[] expected, double[] actual) {
        assertArrayEquals(expected, actual, 0.0);
    }
}