package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair.ComparisonType;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.special.SpecialFunctions;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Test constructor with default parameters and default convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with user-defined convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(0.001, 0.0001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {1.0, 1.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        double[] expected = {2.0, 3.0};
        double[] expectedValue = {0.0, 5.0};
        assertEquals(ComparisonType.EQ, result.getPoint(), expected, 0.001);
        assertEquals(ComparisonType.EQ, MathArrays.toArray(result.getValue()), expectedValue, 0.001);
    }

    /**
     * Test optimization with a function with multiple local minima.
     */
    @Test
    public void testOptimizeFunctionMultipleLocalMinima() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {2.0, 2.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        double[] expected = {1.0, 1.0};
        double[] expectedValue = {0.0, 1.0};
        assertEquals(ComparisonType.EQ, result.getPoint(), expected, 0.001);
        assertEquals(ComparisonType.EQ, MathArrays.toArray(result.getValue()), expectedValue, 0.001);
    }

    /**
     * Test optimization with a function that does not have a global minimum.
     */
    @Test
    public void testOptimizeFunctionNoGlobalMinimum() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return -Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        assertNull(result);
    }

    /**
     * Test optimization with a function that has no solution.
     */
    @Test
    public void testOptimizeFunctionNoSolution() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0) - 4.0;
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        assertNull(result);
    }

    /**
     * Test optimization with a function that has a singular point.
     */
    @Test
    public void testOptimizeFunctionSingularPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2.0) / (x[1] * x[1]);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null));
    }

    /**
     * Test optimization with a function with a large gradient.
     */
    @Test
    public void testOptimizeFunctionLargeGradient() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return SpecialFunctions.gamma(x[0]) * Math.exp(-x[1]);
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        assertNull(result);
    }

    /**
     * Test optimization with a function that has a discontinuity.
     */
    @Test
    public void testOptimizeFunctionDiscontinuity() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {1.0, 1.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 2.0) {
                    return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        };

        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);

        assertNull(result);
    }

    /**
     * Test optimization with a function that has a large search space.
     */
    @Test
    public void testOptimizeFunctionLargeSearchSpace() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        MaxEval maxEval = new MaxEval(100);
        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0}), maxEval);

        double[] expected = {0.5 * Math.PI / 4.0, 0.5 * Math.PI / 4.0};
        double[] expectedValue = {-Math.sqrt(2.0) / 2.0, -Math.sqrt(2.0) / 2.0};
        assertEquals(ComparisonType.NEAR, result.getPoint(), expected, 0.01);
        assertEquals(ComparisonType.NEAR, MathArrays.toArray(result.getValue()), expectedValue, 0.01);
    }

    /**
     * Test optimization with a function that has a large number of dimensions.
     */
    @Test
    public void testOptimizeFunctionHighDimensionality() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = new double[10];
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - i, 2.0);
                }
                return sum;
            }
        };

        MaxEval maxEval = new MaxEval(100);
        PointValuePair result = optimizer.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null, maxEval);

        double[] expected = new double[10];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i;
        }
        double[] expectedValue = new double[1];
        expectedValue[0] = 0.0;
        assertEquals(ComparisonType.ARRAY_EQ, result.getPoint(), expected);
        assertEquals(ComparisonType.EQ, MathArrays.toArray(result.getValue()), expectedValue);
    }

    /**
     * Test optimization with a function that has negative step size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeFunctionNegativeStepSize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {0.0, 0.0};
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        };

        LineSearch lineSearch = new LineSearch(0.001, 0.01, new SimpleUnivariateValueChecker(0.01, 0.001));
        PowellOptimizer optimizerWithCustomLineSearch = new PowellOptimizer(0.01, 0.001, lineSearch);

        optimizerWithCustomLineSearch.optimize(new PointValuePair(startPoint, Double.POSITIVE_INFINITY), function, GoalType.MINIMIZE, null, null, null);
    }

    /**
     * Test optimization with a function that has a negative absolute threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeFunctionNegativeAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -0.001);
    }

    /**
     * Test optimization with a function that has a relative threshold smaller than the machine epsilon.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeFunctionSmallRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(2 * Precision.EPSILON, 0.001);
    }
}