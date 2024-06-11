package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.apache.commons.math3.optimization.TestMultivariateFunction;
import org.junit.Test;

/**
 * JUnit 4 test cases for SimplexOptimizer class.
 */
public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-6;

    /**
     * Test constructor with default ConvergenceChecker.
     */
    @Test
    public void testConstructorDefault() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with SimpleValueChecker.
     */
    @Test
    public void testConstructorSimpleValueChecker() {
        double rel = 1e-6;
        double abs = 1e-6;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(rel, abs));
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with relative and absolute thresholds.
     */
    @Test
    public void testConstructorRelAbs() {
        double rel = 1e-6;
        double abs = 1e-6;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    /**
     * Test setSimplex method with null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        new SimplexOptimizer().setSimplex(null);
    }

    /**
     * Test optimize method with null MultivariateFunction.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, null, GoalType.MINIMIZE, new double[0]);
    }

    /**
     * Test optimize method with null OptimizationData.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, new MultivariateFunction() {}, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with a simple MultivariateFunction.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        TestMultivariateFunction f = new TestMultivariateFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, new OptimizationData() {
            private final double[] startPoint = startPoint;

            @Override
            public double[] getStartPoint() {
                return startPoint;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new SimpleSimplex(3);
            }
        });
        double expected = 0.0;
        assertEquals(expected, result.getValue(), EPSILON);
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }

    /**
     * Test optimization with a simple MultivariateFunction and custom
     * AbstractSimplex.
     */
    @Test
    public void testOptimizeCustomSimplex() {
        TestMultivariateFunction f = new TestMultivariateFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, new OptimizationData() {
            private final double[] startPoint = startPoint;

            @Override
            public double[] getStartPoint() {
                return startPoint;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new CustomSimplex(3);
            }
        });
        double expected = 0.0;
        assertEquals(expected, result.getValue(), EPSILON);
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }

    /**
     * Test optimization with a simple MultivariateFunction and custom
     * ConvergenceChecker.
     */
    @Test
    public void testOptimizeCustomConvergenceChecker() {
        TestMultivariateFunction f = new TestMultivariateFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer(new CustomConvergenceChecker());
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, new OptimizationData() {
            private final double[] startPoint = startPoint;

            @Override
            public double[] getStartPoint() {
                return startPoint;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return new SimpleSimplex(3);
            }
        });
        double expected = 0.0;
        assertEquals(expected, result.getValue(), EPSILON);
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }
}

class CustomSimplex extends AbstractSimplex {
    // Implementation of the custom simplex
}

class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    // Implementation of the custom convergence checker
}

class TestMultivariateFunction implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i], 2);
        }
        return sum;
    }
}