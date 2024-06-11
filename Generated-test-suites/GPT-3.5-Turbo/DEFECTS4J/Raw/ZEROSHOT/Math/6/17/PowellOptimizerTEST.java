import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001);
        
        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Set the start point
        optimizer.setStartPoint(new double[]{0, 0});
        
        // Set the objective function
        optimizer.setObjectiveFunction(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                // return the sum of squares of the elements
                return point[0]*point[0] + point[1]*point[1];
            }
        });
        
        // Set the convergence checker
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // check if the difference between the objective function values is less than 0.001
                return Math.abs(previous.getValue() - current.getValue()) < 0.001;
            }
        });
        
        // Optimize
        PointValuePair result = optimizer.doOptimize();
        
        // Test the result
        assertEquals(result.getPoint()[0], 0.0, 0.001);
        assertEquals(result.getPoint()[1], 0.0, 0.001);
        assertEquals(result.getValue(), 0.0, 0.001);
    }
}