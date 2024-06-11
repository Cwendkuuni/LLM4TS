package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.junit.Test;

/**
 * JUnit 4 test cases for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeThrowsUnsupportedOperationException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeWithMinFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        double expectedX = 2.0;
        double expectedFx = 0.0;
        assertEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedFx, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithMaxFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        double expectedX = 2.0;
        double expectedFx = 0.0;
        assertEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedFx, result.getValue(), 0.0);

        // Negate the function to check minimization results in maximization.
        PointValuePair resultNegatedFunction = optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        }, goalType, min, max, startValue);
        assertEquals(expectedX, resultNegatedFunction.getPoint(), 0.0);
        assertEquals(-expectedFx, resultNegatedFunction.getValue(), 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new FunctionEvaluationException("Function evaluation failed.");
                }
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.1);
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveMaxIterations() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(-1);
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveLowerBound() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 4.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveUpperBound() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = -1.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMinFunctionInfiniteInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMaxFunctionInfiniteInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMinFunctionZeroInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 0.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMaxFunctionZeroInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 0.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMinFunctionZeroStartValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMaxFunctionZeroStartValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMinFunctionDivergingFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }

    @Test
    public void testOptimizeWithMaxFunctionDivergingFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, goalType, min, max, startValue);
            fail();
        } catch (Exception e) {
            // Expected exception.
        }
    }
}