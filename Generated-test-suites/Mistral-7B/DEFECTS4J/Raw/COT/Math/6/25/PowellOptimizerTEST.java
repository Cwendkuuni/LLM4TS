package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNull(optimizer.getConvergenceChecker());
        assertEquals(RelativeTolerance.DEFAULT, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(AbsoluteTolerance.DEFAULT, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testCustomConstructorNoChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        Assert.assertNull(optimizer.getConvergenceChecker());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test
    public void testCustomConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(0.01, 0.001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test
    public void testMinimizeFunctionWithDefaultTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = {-1.0, -1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getValue(), 0.0001);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.0001);
    }

    @Test
    public void testMinimizeFunctionCustomTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.0001);
        double[] startPoint = {-1.0, -1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getValue(), 0.00001);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.00001);
    }

    @Test
    public void testMinimizeFunctionWithCustomChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(0.001, 0.0001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        double[] startPoint = {-1.0, -1.0};
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
            fail("Expected ConvergenceException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConvergenceException);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithNegativeAbsoluteThreshold() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithNegativeRelativeThreshold() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(-0.01, 0.001);
    }

    @Test
    public void testMaximizeFunctionWithDefaultTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = {-1.0, -1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, startPoint);
        assertEquals(0.0, -result.getValue(), 0.0001);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.0001);
    }

    @Test
    public void testMaximizeFunctionCustomTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.0001);
        double[] startPoint = {-1.0, -1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, startPoint);
        assertEquals(0.0, -result.getValue(), 0.00001);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.00001);
    }

    @Test
    public void testMaximizeFunctionWithCustomChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(0.001, 0.0001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        double[] startPoint = {-1.0, -1.0};
        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, startPoint);
            fail("Expected ConvergenceException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConvergenceException);
        }
    }

    @Test
    public void testMinimizeFunctionWithInitialGuessCloseToSolution() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = {0.5, 0.5};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getValue(), 0.0001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0001);
    }

    @Test
    public void testMinimizeFunctionWithInitialGuessFarFromSolution() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = {2.0, 2.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        assertEquals(3.2, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.6, 1.6}, result.getPoint(), 0.001);
    }

    @Test
    public void testMinimizeFunctionWithInitialGuessOutsideDomain() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] < 0) {
                    throw new IllegalArgumentException("x[0] must be non-negative");
                }
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = {-1.0, -1.0};
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testMinimizeFunctionWithCustomLineSearchTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, 0.0001, 0.00001);
        double[] startPoint = {-1.0, -1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getValue(), 0.00001);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.00001);
    }
}