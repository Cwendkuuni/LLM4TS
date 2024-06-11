package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.AbstractUnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test class for the BrentOptimizer class.
 */
@RunWith(Parameterized.class)
public class BrentOptimizerTest {

    /**
     * The arguments are used implement the original stopping criterion
     * of Brent's algorithm.
     * {@code abs} and {@code rel} define a tolerance
     * {@code tol = rel |x| + abs}. {@code rel} should be no smaller than
     * <em>2 macheps</em> and preferably not much less than <em>sqrt(macheps)</em>,
     * where <em>macheps</em> is the relative machine precision. {@code abs} must
     * be positive.
     */
    private Double rel;
    private Double abs;

    /**
     * Constructor.
     *
     * @param rel Relative threshold.
     * @param abs Absolute threshold.
     */
    public BrentOptimizerTest(Double rel, Double abs) {
        this.rel = rel;
        this.abs = abs;
    }

    /**
     * Test data.
     *
     * @return test data.
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0.01, 1.0},
                {0.05, 0.0},
                {0.1, 1.0},
                {1.0, 0.0},
                {10.0, 0.0},
                {100.0, 0.0}
        });
    }

    /**
     * Test constructor with valid arguments.
     *
     * @throws NotStrictlyPositiveException if {@code abs <= 0}.
     * @throws NumberIsTooSmallException if {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test
    public void testConstructorValidArguments() throws NotStrictlyPositiveException, NumberIsTooSmallException {
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    /**
     * Test method optimize.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testOptimize() throws Exception {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define a test function
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x) - 2 * x;
            }
        };

        // Call the optimize method
        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        // Check the result
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(-3.141592653589793, result.getValue(), 1e-5);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidArguments() {
        new BrentOptimizer(-0.1, abs);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, abs);
    }

    /**
     * Test method optimize with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidArguments() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define a test function
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                throw new IllegalArgumentException();
            }
        };

        // Call the optimize method
        optimizer.optimize(function, -10.0, 10.0);
    }

    /**
     * Test method optimize with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullArguments() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Call the optimize method with null arguments
        optimizer.optimize(null, -10.0, 10.0);
    }
}

class AbstractUnivariateFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        throw new UnsupportedOperationException();
    }
}