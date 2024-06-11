import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.00001, 0.0001);
    }
    
    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.8);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 0.6);
        
        boolean isMinim = true;
        
        UnivariatePointValuePair best = optimizer.best(a, b, isMinim);
        assertEquals(best, a);
        
        best = optimizer.best(b, c, isMinim);
        assertEquals(best, c);
        
        best = optimizer.best(a, c, isMinim);
        assertEquals(best, a);
        
        isMinim = false;
        
        best = optimizer.best(a, b, isMinim);
        assertEquals(best, b);
        
        best = optimizer.best(b, c, isMinim);
        assertEquals(best, b);
        
        best = optimizer.best(a, c, isMinim);
        assertEquals(best, c);
        
        best = optimizer.best(null, null, isMinim);
        assertNull(best);
        
        best = optimizer.best(a, null, isMinim);
        assertEquals(best, a);
        
        best = optimizer.best(null, b, isMinim);
        assertEquals(best, b);
    }
}