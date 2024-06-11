package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.util.TestArrayRealVector;
import org.apache.commons.math3.optim.util.TestFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-0.01, 0.01);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.01, 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, testFunction,
                GoalType.MINIMIZE, 0, 1);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.5);
        assertEquals(a, optimizer.best(a, null, true));
        assertEquals(a, optimizer.best(null, a, true));
        assertEquals(a, optimizer.best(a, b, true));
        assertNull(optimizer.best(null, null, true));
        assertEquals(b, optimizer.best(null, b, true));
        assertEquals(b, optimizer.best(b, null, true));
        assertEquals(a, optimizer.best(a, b, false));
        assertNull(optimizer.best(null, null, false));
        assertEquals(a, optimizer.best(null, a, false));
        assertEquals(a, optimizer.best(a, null, false));
    }
    
    @Test
    public void testEquality() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertTrue(optimizer.equals(optimizer));
        assertFalse(optimizer.equals(null));
        assertFalse(optimizer.equals(new Object()));
    }
}