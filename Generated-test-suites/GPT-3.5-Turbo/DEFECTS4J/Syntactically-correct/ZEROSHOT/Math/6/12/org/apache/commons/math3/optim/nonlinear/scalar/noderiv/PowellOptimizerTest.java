import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        //Initialize PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        //Set relative threshold and absolute threshold
        double relThreshold = 1e-6;
        double absThreshold = 1e-6;
        
        //Set starting point
        double[] startPoint = {0, 0};
        optimizer.setStartPoint(startPoint);
        
        //Set goal type
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.setGoalType(goalType);
        
        //Set convergence checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(relThreshold, absThreshold);
        optimizer.setConvergenceChecker(checker);
        
        //Set objective function
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        optimizer.setObjectiveFunction(objectiveFunction);
        
        //Run optimization
        PointValuePair result = optimizer.optimize();
        
        //Check if result is correct
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }
}