import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 1e-10;
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Test if result values are within expected range
        assertTrue(result.getValue() > -Double.MAX_VALUE);
        assertTrue(result.getValue() < Double.MAX_VALUE);

        // Test if result point is within boundaries
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
    }

    @Test
    public void testNegativeValues() {
        double rel = 1e-10;
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set the minimum value to a negative number
        double min = -10;
        optimizer.setMin(min);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Test if result point is within boundaries
        assertTrue(result.getPoint() >= min);
        assertTrue(result.getPoint() <= optimizer.getMax());
    }

    @Test
    public void testLargeValues() {
        double rel = 1e-10;
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set the maximum value to a large number
        double max = 1000000;
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Test if result point is within boundaries
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= max);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroAbsValue() {
        double rel = 1e-10;
        double abs = 0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelValue() {
        double rel = 1e-20; // Relatively small value
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testConvergenceChecker() {
        double rel = 1e-10;
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Create a custom convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                double diff = Math.abs(current.getValue() - previous.getValue());
                return diff < 1e-8; // Check if the difference between consecutive values is below a threshold
            }
        };

        optimizer.setConvergenceChecker(checker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Test if the convergence checker successfully converged
        assertTrue(result.getValue() < 1e-8);
    }

}