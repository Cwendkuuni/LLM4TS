package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the BrentOptimizer class.
 */
public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-7;
    private static final double MIN_VALUE = -2.5;
    private static final double MAX_VALUE = 2.5;
    private static final double EPSILON = 1e-10;

    private UnivariateFunction function;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        function = f -> Math.pow(Math.sin(Math.PI * f), 2);
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    /**
     * Test the constructor with valid parameters.
     */
    @Test
    public void testConstructorValidParameters() {
        assertInstanceOf(BrentOptimizer.class, optimizer);
    }

    /**
     * Test the constructor with invalid absolute threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidAbsoluteThreshold() {
        new BrentOptimizer(RELATIVE_TOLERANCE, -ABSOLUTE_TOLERANCE);
    }

    /**
     * Test the constructor with invalid relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRelativeThreshold() {
        new BrentOptimizer(-RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    /**
     * Test the optimization with a simple function.
     */
    @Test
    public void testOptimizationSimpleFunction() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(function);
        optimizer.setInterval(MIN_VALUE, MAX_VALUE);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(-1, result.getPoint(), EPSILON);
        assertEquals(MIN_VALUE, result.getValue(), EPSILON);
    }

    /**
     * Test the optimization with a function having multiple local minima.
     */
    @Test
    public void testOptimizationMultipleLocalMinima() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(f -> (f - 1) * (f - 1) + 0.1);
        optimizer.setInterval(-3, 3);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(-1, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    /**
     * Test the optimization with a function that is not monotonic.
     */
    @Test
    public void testOptimizationNonMonotonicFunction() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(f -> Math.sin(Math.PI * f) * Math.cos(Math.PI * f));
        optimizer.setInterval(-5, 5);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0, result.getPoint(), EPSILON);
        assertEquals(0, result.getValue(), EPSILON);
    }

    /**
     * Test the optimization with a convergent function.
     */
    @Test
    public void testOptimizationConvergentFunction() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(f -> Math.pow(f, 3) - 2 * f);
        optimizer.setInterval(-3, 3);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 10;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(-1.1213203435596426, result.getPoint(), EPSILON);
        assertEquals(-1.1213203435596426, result.getValue(), EPSILON);
    }

    /**
     * Test the optimization with a non-convergent function.
     */
    @Test(expected = AssertionError.class)
    public void testOptimizationNonConvergentFunction() {
        optimizer.setMinimizer(true);
        optimizer.setFunction(f -> Math.sin(Math.PI * f) / Math.cos(Math.PI * f));
        optimizer.setInterval(-5, 5);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });
        optimizer.optimize();
    }
}