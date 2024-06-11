package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.00001, 0.00001);
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelTolerance() {
        new BrentOptimizer(0.000000001, 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsTolerance() {
        new BrentOptimizer(0.00001, 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(2.0, result.getPoint(), 0.0001);
        assertEquals(5.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestEqualValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 5.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(5.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestNotMinimizing() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(10.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testPrecisionEquals() {
        assertTrue(Precision.equals(1.0, 1.0, 0.0001));
        assertFalse(Precision.equals(1.0, 2.0, 0.0001));
    }
}