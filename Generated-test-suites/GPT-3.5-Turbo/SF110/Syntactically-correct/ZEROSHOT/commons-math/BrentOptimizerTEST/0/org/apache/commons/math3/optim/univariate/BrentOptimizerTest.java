import org.junit.Test;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentOptimizerTest {
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallException() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 1.0, 1.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        new BrentOptimizer(0.5, 0.0);
    }
    
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        
        // Set up mock objects
        UnivariateObjectiveFunction objectiveFunction = mock(UnivariateObjectiveFunction.class);
        when(objectiveFunction.value(anyDouble())).thenReturn(2.0);
        
        optimizer.withObjectiveFunction(objectiveFunction);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 0.0);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(1.0, result.getValue(), 0.0);
        
        result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 0.0);
    }
    
}