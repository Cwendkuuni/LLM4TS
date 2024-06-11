import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        new BrentOptimizer(0.0, 1.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        new BrentOptimizer(1.0, 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
                
        UnivariatePointValuePair result = optimizer.doOptimize();
                
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestWithNull() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestWithBothNonNullMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestWithBothNonNullMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestWithOneNullMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = null;
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBestWithOneNullMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = null;
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}