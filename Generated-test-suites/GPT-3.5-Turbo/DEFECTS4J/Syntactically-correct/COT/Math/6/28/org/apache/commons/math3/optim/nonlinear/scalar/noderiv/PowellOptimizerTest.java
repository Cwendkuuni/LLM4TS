
import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Instantiate PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(1, 1);

        // Define function
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                double x = variables[0];
                double y = variables[1];
                return Math.pow(x - 1, 2) + Math.pow(y - 2, 2);
            }
        };

        // Set optimization problem
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(new double[]{0, 0});

        // Perform optimization
        PointValuePair result = optimizer.doOptimize();

        // Check result
        assertEquals(1.0, result.getPoint()[0], 0.0001);
        assertEquals(2.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}
