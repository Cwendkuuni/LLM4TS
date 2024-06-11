package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizationProblem;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    private UnivariateRealFunction testFunction;
    private UnivariateOptimizationProblem testProblem;

    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        testProblem = new UnivariateOptimizationProblem(testFunction, 0.0);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getResult());
    }

    /**
     * Test method doOptimize.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    /**
     * Test method optimize.
     */
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 10.0, 1.0);
        assertEquals(Math.PI, solution.getPoint(), 0.001);
        assertEquals(-3.141592653589793, solution.getValue(), 0.01);
    }

    /**
     * Test method optimize with minimum and maximum bounds equal.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeMinMaxSame() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0);
    }

    /**
     * Test method optimize with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidArguments() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, -1.0, 1.0);
    }

    /**
     * Test method optimize with relative accuracy less than machine epsilon.
     */
    @Test
    public void testOptimizeRelativeAccuracyLessThanMachineEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(1e-15);
        PointValuePair solution = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 10.0, 1.0);
        assertArrayEquals(new double[]{Math.PI, -3.141592653589793}, new double[]{solution.getPoint(), solution.getValue()}, 0.001);
    }

    /**
     * Test method optimize with absolute accuracy less than machine epsilon.
     */
    @Test
    public void testOptimizeAbsoluteAccuracyLessThanMachineEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1e-15);
        PointValuePair solution = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 10.0, 1.0);
        assertArrayEquals(new double[]{Math.PI, -3.141592653589793}, new double[]{solution.getPoint(), solution.getValue()}, 0.001);
    }

    /**
     * Test method optimize with invalid goal type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeInvalidGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(testFunction, GoalType.NEUTRAL, 0.0, 10.0, 1.0);
    }

    /**
     * Test method localMin.
     */
    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-6, 1e-6);
        assertEquals(Math.PI, result, 0.001);
    }

    /**
     * Test method localMin with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidArguments() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, -1.0, 1e-6, 1e-6);
    }

    /**
     * Test method localMin with excessive number of iterations.
     */
    @Test
    public void testLocalMinMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        try {
            optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-6, 1e-6);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}