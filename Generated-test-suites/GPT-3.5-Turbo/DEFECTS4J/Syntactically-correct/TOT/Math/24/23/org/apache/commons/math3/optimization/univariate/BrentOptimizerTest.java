import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    // Test for doOptimize method
    @Test
    public void testDoOptimize() {
        // Create the optimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        // Test for a simple quadratic function f(x) = x^2
        optimizer.setObjectiveFunction((x) -> x * x);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.001);

        // Test for a simple linear function f(x) = x
        optimizer.setObjectiveFunction((x) -> x);
        result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.001);

        // Test for a function with multiple local minima
        optimizer.setObjectiveFunction((x) -> Math.sin(x));
        result = optimizer.doOptimize();
        assertEquals(-1.0, result.getValue(), 0.001);

        // Test for a function with local and global minima
        optimizer.setObjectiveFunction((x) -> Math.pow(x - 3, 2) * Math.pow(x + 2, 2));
        result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.001);
    }

    // Test for best method
    @Test
    public void testBest() {
        // Create the optimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        // Test when both points are null
        UnivariatePointValuePair result = optimizer.best(null, null, true);
        assertNull(result);

        // Test when one point is null
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 5.0);
        result = optimizer.best(a, null, true);
        assertEquals(a, result);

        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 3.0);
        result = optimizer.best(null, b, true);
        assertEquals(b, result);

        // Test when both points are not null
        result = optimizer.best(a, b, true);
        assertEquals(b, result);

        result = optimizer.best(a, b, false);
        assertEquals(a, result);
    }

    // Add additional test cases for all other methods in BrentOptimizer class

}