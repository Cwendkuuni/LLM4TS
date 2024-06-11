import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        
        UnivariatePointValuePair expectedPoint = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair actualPoint = optimizer.doOptimize();
        
        assertEquals(expectedPoint, actualPoint);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        
        double minValue = 1.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.0);
        
        UnivariatePointValuePair expectedBest = a;
        UnivariatePointValuePair actualBest = optimizer.best(a, b, true);
        
        assertEquals(expectedBest, actualBest);
    }
    
    // Additional test cases for other methods in BrentOptimizer class can be added here
    
}