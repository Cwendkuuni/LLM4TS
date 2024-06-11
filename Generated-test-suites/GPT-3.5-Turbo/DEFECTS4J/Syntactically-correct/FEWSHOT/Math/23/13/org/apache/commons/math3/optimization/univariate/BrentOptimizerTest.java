
package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }
    
    @Test
    public void testDoOptimize() {
        assertEquals(1.0, optimizer.doOptimize().getValue(), 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallException() {
        optimizer = new BrentOptimizer(0.0, 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        optimizer = new BrentOptimizer(1e-6, -1e-6);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        
        assertNotNull(optimizer.best(a, b, true));
        assertNotNull(optimizer.best(a, null, true));
        assertNull(optimizer.best(null, null, true));
        
        assertEquals(1.0, optimizer.best(a, b, true).getValue(), 1e-6);
    }
}
