import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize_StartValue() {
        // Test optimization with start value
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, 0.0, 3.0, 1.5);
        assertEquals(0.0, result, 1e-10);
    }
    
    @Test
    public void testOptimize_NoStartValue() {
        // Test optimization without start value
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MAXIMIZE, -3.0, 3.0);
        assertEquals(9.0, result, 1e-10);
    }
    
    @Test
    public void testLocalMin() {
        // Test local minimum calculation
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = x -> x + Math.sin(x);
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-12, 1e-10);
        assertEquals(0.0, result, 1e-10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidEps() {
        // Test exception for invalid epsilon in local minimum calculation
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x, GoalType.MINIMIZE, 0.0, 1.0, 2.0, -1e-12, 1e-10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidT() {
        // Test exception for invalid t in local minimum calculation
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-12, -1e-10);
    }
}