package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizationFunction;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.MinimizationType;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.FunctionException;

import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Function for testing.
     */
    private static final UnivariateOptimizationFunction FUNCTION = new UnivariateOptimizationFunction() {

        @Override
        public double value(double x) throws FunctionException {
            double result = Math.exp(x) - E.E27;
            return result;
        }

        @Override
        public double derivative(double x) throws FunctionException {
            return value(x) * Math.exp(x);
        }

        @Override
        public void checkDerivative(double x, double expected) {
            assertEquals(expected, value(x) * Math.exp(x), 1e-10);
        }
    };

    /**
     * Test constructor with relation and absolute tolerance and null
     * convergence checker.
     *
     * @throws NotStrictlyPositiveException if the tolerance values are not
     * positive.
     */
    @Test
    public void testConstructorWithRelAndAbsTolerance()
            throws NotStrictlyPositiveException {
        double relTolerance = 1e-5;
        double absTolerance = 1e-10;

        BrentOptimizer optimizer = new BrentOptimizer(relTolerance, absTolerance);
        assertNotNull(optimizer);
        assertEquals(relTolerance, optimizer.getRelativeThreshold(), 1e-15);
        assertEquals(absTolerance, optimizer.getAbsoluteThreshold(), 1e-15);
    }

    /**
     * Test constructor with relation and absolute tolerance and custom
     * convergence checker.
     *
     * @throws NotStrictlyPositiveException if the tolerance values are not
     * positive.
     */
    @Test
    public void testConstructorWithRelAndAbsToleranceAndChecker()
            throws NotStrictlyPositiveException {
        double relTolerance = 1e-5;
        double absTolerance = 1e-10;
        MinimizationType goalType = MinimizationType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer(relTolerance, absTolerance,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                });
        assertNotNull(optimizer);
        assertEquals(relTolerance, optimizer.getRelativeThreshold(), 1e-15);
        assertEquals(absTolerance, optimizer.getAbsoluteThreshold(), 1e-15);
    }

    /**
     * Test constructor with relation tolerance and no absolute tolerance.
     *
     * @throws NotStrictlyPositiveException if the tolerance value is not
     * positive.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithOnlyRelTolerance()
            throws NotStrictlyPositiveException {
        double relTolerance = 1e-15;
        new BrentOptimizer(relTolerance, 0.0);
    }

    /**
     * Test constructor with no relation tolerance and absolute tolerance.
     *
     * @throws NotStrictlyPositiveException if the absolute tolerance value is
     * not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithOnlyAbsTolerance()
            throws NotStrictlyPositiveException {
        new BrentOptimizer(0.0, -1e-10);
    }

    /**
     * Test optimization of a dummy function with default stopping criterion.
     */
    @Test
    public void testOptimizeWithDefaultStoppingCriterion() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6);
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with user-defined stopping criterion.
     */
    @Test
    public void testOptimizeWithUserDefinedStoppingCriterion() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                });
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given minimum and maximum.
     */
    @Test
    public void testOptimizeWithMinAndMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6);
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given initial value.
     */
    @Test
    public void testOptimizeWithInitialValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6);
        PointValuePair result = optimizer.optimize(FUNCTION, 1.0, null, null);
        assertArrayEquals(new double[]{2.7182818284590451,
                -1.1312110061495544}, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given goal type.
     */
    @Test
    public void testOptimizeWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                }, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given initial value and goal type.
     */
    @Test
    public void testOptimizeWithInitialValueAndGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                }, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(FUNCTION, 1.0, null, null);
        assertArrayEquals(new double[]{2.7182818284590451,
                -1.1312110061495544}, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given initial value, goal type,
     * and user-defined convergence criterion.
     */
    @Test
    public void testOptimizeWithInitialValueAndGoalTypeAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                }, GoalType.MAXIMIZE,
                new ValueIterationChecker() {

                    @Override
                    public boolean converged(int iteration,
                            PointValuePair previous, PointValuePair current) {
                        return iteration > 10;
                    }
                });
        PointValuePair result = optimizer.optimize(FUNCTION, 1.0, null, null);
        assertArrayEquals(new double[]{2.7182818284590451,
                -1.1312110061495544}, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given minimum, maximum, and
     * user-defined convergence criterion.
     */
    @Test
    public void testOptimizeWithMinMaxAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                }, GoalType.MAXIMIZE,
                new ValueIterationChecker() {

                    @Override
                    public boolean converged(int iteration,
                            PointValuePair previous, PointValuePair current) {
                        return iteration > 10;
                    }
                });
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }

    /**
     * Test optimization of a dummy function with given minimum, maximum,
     * initial value, goal type, and user-defined convergence criterion.
     */
    @Test
    public void testOptimizeWithMinMaxInitialValueGoalTypeAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6,
                new ValueInitializer() {

                    @Override
                    public PointValuePair initialize() {
                        return new PointValuePair(0.0, Double.POSITIVE_INFINITY);
                    }
                }, GoalType.MAXIMIZE,
                new ValueIterationChecker() {

                    @Override
                    public boolean converged(int iteration,
                            PointValuePair previous, PointValuePair current) {
                        return iteration > 10;
                    }
                });
        PointValuePair result = optimizer.optimize(FUNCTION, 0.0, 1.0, null);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, result.getPoint(),
                0);
        assertEquals(-1.1312110061495544, result.getValue(), 1e-12);
    }
}

class ValueIterationChecker implements ConvergenceChecker<PointValuePair> {

    @Override
    public boolean converged(int iteration, PointValuePair previous,
            PointValuePair current) {
        return iteration > 10;
    }
}