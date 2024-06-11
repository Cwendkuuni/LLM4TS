import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        // Test 1
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);

        // Test 2
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setStartValue(2);
        result = optimizer.doOptimize();
        assertEquals(2, result.getPoint(), 1e-6);

        // Test 3
        optimizer.setMin(-2);
        optimizer.setMax(2);
        optimizer.setStartValue(-1);
        result = optimizer.doOptimize();
        assertEquals(-1, result.getPoint(), 1e-6);

        // Test 4
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        result = optimizer.doOptimize();
        assertEquals(5, result.getPoint(), 1e-6);

        // Test 5
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);
    }
}