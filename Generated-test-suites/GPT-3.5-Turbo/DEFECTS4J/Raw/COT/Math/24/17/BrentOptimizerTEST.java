import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(-1.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
        
        // Test case 2
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        result = optimizer.optimize(100, Math::sin);
        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
        
        // Test case 3
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        result = optimizer.optimize(100, Math::log);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
        
        // Test case 4
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(-5.0);
        result = optimizer.optimize(100, Math::log);
        assertEquals(Double.NaN, result.getPoint(), 1e-6);
        assertEquals(Double.NaN, result.getValue(), 1e-6);
    }
}