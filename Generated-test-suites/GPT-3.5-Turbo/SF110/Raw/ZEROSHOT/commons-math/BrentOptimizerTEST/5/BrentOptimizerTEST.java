import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testDoOptimize() {
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(mid, result.getPoint(), 0);
        assertEquals(0.0, result.getValue(), 0);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0);
        assertEquals(0.0, result.getValue(), 0);
    }
}