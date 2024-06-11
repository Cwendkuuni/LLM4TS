package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.GoalType;

public class BrentOptimizerTest {
    
    @Test
    public void testBrentOptimizerConstructor() {
        try {
            new BrentOptimizer(-0.1, 0.1);
            fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            // pass
        }
        
        try {
            new BrentOptimizer(0.1, -0.1);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // pass
        }
    }
    
    @Test
    public void testBestMethod() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);
        
        assertEquals(a, optimizer.best(a, null, true));
        assertEquals(b, optimizer.best(null, b, true));
        assertEquals(a, optimizer.best(a, b, true));
        assertEquals(c, optimizer.best(c, a, true));
        
        assertEquals(b, optimizer.best(a, null, false));
        assertEquals(a, optimizer.best(null, b, false));
        assertEquals(b, optimizer.best(a, b, false));
        assertEquals(c, optimizer.best(c, a, false));
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(1.0, result.getValue(), 1e-10);
    }
    
    @Test
    public void testDoOptimize_nullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(1.0, result.getValue(), 1e-10);
    }
}