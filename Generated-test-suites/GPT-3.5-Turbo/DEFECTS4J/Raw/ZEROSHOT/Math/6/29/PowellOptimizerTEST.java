import org.apache.commons.math3.testing.TestUtils;
import org.junit.Test;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        
        // Set the start point
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        
        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Set the objective function
        optimizer.setObjectiveFunction(new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        });
        
        // Set the convergence checker
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double diff = Math.abs(previous.getValue() - current.getValue());
                return diff < 1e-6;
            }
        });
        
        // Perform optimization
        PointValuePair result = optimizer.optimize();
        
        // Check the result
        double[] expectedPoint = new double[]{0.0, 0.0, 0.0};
        double expectedValue = 0.0;
        TestUtils.assertEquals(expectedPoint, result.getPoint(), 1e-6);
        TestUtils.assertEquals(expectedValue, result.getValue(), 1e-6);
    }
}