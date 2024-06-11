import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_MinimizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        double result = optimizer.doOptimize();

        // Add assertions here to verify the result for minimizing a function
    }

    @Test
    public void testDoOptimize_MaximizeFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 50;
            }
        });

        double result = optimizer.doOptimize();

        // Add assertions here to verify the result for maximizing a function
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-10, -1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdTooSmall() {
        new BrentOptimizer(1e-15, 1e-10);
    }

    // Add more test cases for edge cases and error scenarios as needed

}