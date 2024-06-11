package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;

/**
 * JUnit test class for {@link BrentOptimizer} class.
 *
 * @author John Smith
 */
public class BrentOptimizerTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        final BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    /**
     * Test doOptimize method throws UnsupportedOperationException.
     */
    @org.junit.Test
    public void testDoOptimizeThrowsUOE() {
        final BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(UnsupportedOperationException.class, optimizer::doOptimize);
    }

    /**
     * Test optimize method with valid arguments.
     */
    @org.junit.Test
    public void testOptimizeValidArguments() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double expectedResult = 2.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, 3.0);

        assertEquals(expectedResult, result, 1e-6);
    }

    /**
     * Test optimize method with invalid arguments - min > max.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsMinGreaterThanMax() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 4.0;
        final double max = 0.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, 3.0));
    }

    /**
     * Test optimize method with invalid arguments - function is null.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsFunctionIsNull() {
        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(NullPointerException.class, () -> optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.0));
    }

    /**
     * Test optimize method with invalid arguments - goalType is null.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsGoalTypeIsNull() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(NullPointerException.class, () -> optimizer.optimize(function, null, min, max, 3.0));
    }

    /**
     * Test optimize method with invalid arguments - startValue is less than min.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsStartValueLessThanMin() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue));
    }

    /**
     * Test optimize method with invalid arguments - maxIterations is less than zero.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsMaxIterationsLessThanZero() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = 2.0;
        final int maxIterations = -1;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue, maxIterations));
    }

    /**
     * Test optimize method with invalid arguments - absAccuracy is less than zero.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsAbsAccuracyLessThanZero() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = 2.0;
        final int maxIterations = 100;
        final double absAccuracy = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class,
                     () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue, maxIterations, absAccuracy));
    }

    /**
     * Test optimize method with invalid arguments - relAccuracy is less than zero.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsRelAccuracyLessThanZero() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = 2.0;
        final int maxIterations = 100;
        final double absAccuracy = 1.0e-10;
        final double relAccuracy = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class,
                     () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue, maxIterations, absAccuracy, relAccuracy));
    }

    /**
     * Test optimize method with invalid arguments - maxEvaluations is less than zero.
     */
    @org.junit.Test
    public void testOptimizeInvalidArgumentsMaxEvaluationsLessThanZero() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = 2.0;
        final int maxIterations = 100;
        final double absAccuracy = 1.0e-10;
        final double relAccuracy = 1.0e-14;
        final int maxEvaluations = -1;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class,
                     () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue, maxIterations, absAccuracy, relAccuracy, maxEvaluations));
    }

    /**
     * Test optimize method with function that throws FunctionEvaluationException.
     */
    @org.junit.Test
    public void testOptimizeFunctionEvaluationException() {
        final UnivariateRealFunction function = (x) -> {
            throw new FunctionEvaluationException("Test FunctionEvaluationException");
        };
        final double min = 0.0;
        final double max = 4.0;
        final double startValue = 2.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue));
    }

    /**
     * Test optimize method with function that throws MaxIterationsExceededException.
     */
    @org.junit.Test
    public void testOptimizeMaxIterationsExceededException() {
        final UnivariateRealFunction function = (x) -> Math.sqrt(Math.abs(x));
        final double min = -1.0;
        final double max = 1.0;
        final double startValue = 0.0;
        final int maxIterations = 1;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue, maxIterations));
    }

    /**
     * Test optimize method with function that throws NotStrictlyPositiveException.
     */
    @org.junit.Test
    public void testOptimizeNotStrictlyPositiveException() {
        final UnivariateRealFunction function = (x) -> Math.pow(x, 2);
        final double min = -1.0;
        final double max = 1.0;
        final double startValue = 0.0;

        final BrentOptimizer optimizer = new BrentOptimizer();

        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue));
    }
}