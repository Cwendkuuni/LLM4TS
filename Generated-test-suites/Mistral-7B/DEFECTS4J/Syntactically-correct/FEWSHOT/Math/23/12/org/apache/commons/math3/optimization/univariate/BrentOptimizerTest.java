package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltypes.GoalType;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateFunction testFunction;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    @Test
    public void testBrentOptimizer() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-3.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerAbsoluteThreshold() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setAbsoluteThreshold(0.1);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-3.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerRelativeThreshold() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setRelativeThreshold(1e-3);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 1e-3);
        assertEquals(-3.0, result.getValue(), 1e-3);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setConvergenceChecker((iter, a, b) -> iter > 100);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerNoImprovement() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        });
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerCustomIterationLimit() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setIterationLimit(10);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-3.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerCustomInitPoint() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setInitialPoint(3.0);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-3.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerGoalType() {
        optimizer.setMaximizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        });
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(Math.E, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerNoImprovementAfterIterationLimit() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(testFunction);
        optimizer.setIterationLimit(2);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerGoldenSectionStep() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x - 1) + 1;
            }
        });
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerParabolicInterpolationStep() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        });
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerInvalidArguments() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-0.1, 1.0));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.1, -0.1));
    }

    @Test
    public void testBrentOptimizerWeakConvergenceChecker() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 1);
            }
        });
        optimizer.setConvergenceChecker((iter, a, b) -> iter > 10);
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerStrictConvergenceChecker() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 1);
            }
        });
        optimizer.setConvergenceChecker((iter, a, b) -> {
            if (iter > 10) {
                return true;
            }
            if (Math.abs(b.getValue() - a.getValue()) < 1e-6 && Math.abs(b.getPoint() - a.getPoint()) < 1e-6) {
                return true;
            }
            return false;
        });
        optimizer.optimize();
        UnivariatePointValuePair result = optimizer.getOptimum();
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }
}