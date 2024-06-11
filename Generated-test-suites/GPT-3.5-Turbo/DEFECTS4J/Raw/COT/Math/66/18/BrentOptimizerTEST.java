import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test case 1: Basic functionality test
        double result = optimizer.optimize(new UnivariateRealFunction() {
        	public double value(double x) {
        		return x * x; // Quadratic function
        	}
        }, GoalType.MINIMIZE, 0, 10);
        assertEquals(0, result, 1E-10);
        
        // Test case 2: Test with a non-quadratic function
        result = optimizer.optimize(new UnivariateRealFunction() {
        	public double value(double x) {
        		return Math.sin(x); // Sin function
        	}
        }, GoalType.MAXIMIZE, 0, Math.PI);
        assertEquals(Math.PI/2, result, 1E-10);
        
        // Test case 3: Test edge case with equal lower and upper bounds
        result = optimizer.optimize(new UnivariateRealFunction() {
        	public double value(double x) {
        		return x; // Linear function
        	}
        }, GoalType.MINIMIZE, 5, 5);
        assertEquals(5, result, 1E-10);
    }
}