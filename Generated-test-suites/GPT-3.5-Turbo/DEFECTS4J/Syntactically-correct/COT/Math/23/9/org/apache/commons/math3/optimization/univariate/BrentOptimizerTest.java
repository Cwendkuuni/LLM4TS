
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.001);

        // Set the properties of the optimizer
        optimizer.setMin(0);
        optimizer.setMax(100);
        optimizer.setStartValue(50);

        // Set the convergence checker
        optimizer.setConvergenceChecker(new MyConvergenceChecker());

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Verify the result
        assertNotNull(result);
    }
    
    private class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iteration >= 10;
        }
    }
}
