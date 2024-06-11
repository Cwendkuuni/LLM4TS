import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);

        // Set the objective function and initial guess
        optimizer.setObjectiveFunction((x) -> x * x);
        optimizer.setStartValue(2.0);

        // Optimize and get the result
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check that the result is as expected
        assertEquals(0.0, result.getValue(), 1e-8);
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testBest() {
        // Create some test point-value pairs
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 7.0);

        // Test minimization
        boolean isMinim = true;
        UnivariatePointValuePair result = best(a, b, isMinim);
        assertEquals(b, result);

        // Test maximization
        isMinim = false;
        result = best(a, b, isMinim);
        assertEquals(a, result);
    }
}