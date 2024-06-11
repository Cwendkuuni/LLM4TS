import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testBrentOptimizer() {
        // Create a new BrentOptimizer with relative threshold 0.1 and absolute threshold 0.01
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithNegativeRelativeThreshold() {
        // Try to create a new BrentOptimizer with negative relative threshold (-0.1) and absolute threshold 0.01
        BrentOptimizer optimizer = new BrentOptimizer(-0.1, 0.01);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbsoluteThreshold() {
        // Try to create a new BrentOptimizer with relative threshold 0.1 and zero absolute threshold 0.00
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.00);
    }
    
    @Test
    public void testDoOptimize() {
        // Create a new BrentOptimizer with relative threshold 0.1 and absolute threshold 0.01
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        
        // Set the objective function to x^2
        optimizer.setFunction(x -> Math.pow(x, 2));
        
        UnivariatePointValuePair result = optimizer.optimize(min, max, startValue);
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0);
        assertEquals(0.0, result.getValue(), 0);
    }
}