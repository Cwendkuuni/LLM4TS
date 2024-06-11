package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.simple.AbstractUnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    private UnivariateFunction f;

    @Before
    public void setUp() {
        f = new AbstractUnivariateFunction("exp(-x^2)", x -> Math.exp(-Math.pow(x, 2)));
    }

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertFalse(optimizer.isFindMin());
        assertFalse(optimizer.isFindMax());
        assertNull(optimizer.doOptimize());
    }

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(f);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setFindMin(true);
        PointValuePair result = optimizer.optimize();
        assertEquals(-2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(-1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeNoInitialValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(f);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setFindMin(true);
        PointValuePair result = optimizer.optimize();
        assertEquals(-2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(-1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(f);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setFindMin(true);
        optimizer.setConvergenceChecker((iter, previous, current) -> {
            assertNotNull(previous);
            assertNotNull(current);
            assertEquals(-2.44948974278317, previous.getPoint(), 1e-4);
            assertEquals(-1.50808133331294E-7, previous.getValue(), 1e-7);
            assertEquals(-2.44948974278317, current.getPoint(), 1e-4);
            assertEquals(-1.50808133331294E-7, current.getValue(), 1e-7);
            return iter >= 10;
        });
        PointValuePair result = optimizer.optimize();
        assertEquals(-2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(-1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(x -> Math.exp(-Math.pow(x, 2)));
        optimizer.setMin(5);
        optimizer.setMax(-5);
        optimizer.setFindMin(false);
        PointValuePair result = optimizer.optimize();
        assertEquals(2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximizeNoInitialValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(x -> Math.exp(-Math.pow(x, 2)));
        optimizer.setMin(5);
        optimizer.setMax(-5);
        optimizer.setFindMin(false);
        PointValuePair result = optimizer.optimize();
        assertEquals(2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximizeWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(x -> Math.exp(-Math.pow(x, 2)));
        optimizer.setMin(5);
        optimizer.setMax(-5);
        optimizer.setFindMin(false);
        optimizer.setConvergenceChecker((iter, previous, current) -> {
            assertNotNull(previous);
            assertNotNull(current);
            assertEquals(2.44948974278317, previous.getPoint(), 1e-4);
            assertEquals(1.50808133331294E-7, previous.getValue(), 1e-7);
            assertEquals(2.44948974278317, current.getPoint(), 1e-4);
            assertEquals(1.50808133331294E-7, current.getValue(), 1e-7);
            return iter >= 10;
        });
        PointValuePair result = optimizer.optimize();
        assertEquals(2.44948974278317, result.getPoint(), 1e-4);
        assertEquals(1.50808133331294E-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerWithSmallTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-15, 1e-15);
        optimizer.setFunction(x -> Math.exp(-Math.pow(x, 2)));
        optimizer.setMin(5);
        optimizer.setMax(-5);
        optimizer.setFindMin(false);
        PointValuePair result = optimizer.optimize();
        assertEquals(2.44948974278317, result.getPoint(), 1e-15);
        assertEquals(1.50808133331294E-7, result.getValue(), 1e-15);
    }

    @Test
    public void testBrentOptimizerWithBigFunctionRange() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.setFunction(x -> E.EulerNumber().exp(x));
        optimizer.setMin(-10 * E.EPSILON);
        optimizer.setMax(10 * E.EPSILON);
        optimizer.setFindMin(false);
        PointValuePair result = optimizer.optimize();
        assertEquals(0, result.getPoint(), 1e-4);
        assertEquals(1, result.getValue(), 1e-7);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-4, 0);
        optimizer.setFunction(f);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setFindMin(true);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-4);
        optimizer.setFunction(f);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setFindMin(true);
    }
}