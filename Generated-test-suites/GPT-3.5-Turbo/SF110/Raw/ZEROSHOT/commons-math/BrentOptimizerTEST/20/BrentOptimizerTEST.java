package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.001, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
    }
    
    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertSame(b, result);
    }
    
    @Test
    public void testBestWithNull() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertSame(a, result);
    }
}