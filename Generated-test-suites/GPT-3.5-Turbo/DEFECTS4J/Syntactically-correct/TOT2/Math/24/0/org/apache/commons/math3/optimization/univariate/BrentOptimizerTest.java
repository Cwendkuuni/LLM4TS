import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        // Set the function to be minimized
        optimizer.setObjective(new UnivariateObjectiveFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        });
        
        // Set the search interval
        optimizer.setMin(0);
        optimizer.setMax(5);
        
        // Set the start value
        optimizer.setStartValue(4);
        
        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Call the optimization method
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Check the result
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    // Add more test cases for other methods as needed

}