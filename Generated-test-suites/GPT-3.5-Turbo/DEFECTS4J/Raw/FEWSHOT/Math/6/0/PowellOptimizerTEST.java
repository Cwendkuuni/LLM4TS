import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    private static final double EPSILON = 1e-6;

    @Test
    public void testDoOptimize_minimize() {
        // Objective function: f(x, y) = x^2 + y^2
        MultivariateFunction objective = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };

        // Create the optimizer
        double relativeThreshold = 1e-3;
        double absoluteThreshold = 1e-6;
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);

        // Set the objective function
        optimizer.setObjective(objective);

        // Set the start point
        double[] startPoint = {1, 2};
        optimizer.setStartPoint(startPoint);

        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        for (int i = 0; i < startPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], EPSILON);
        }
        assertEquals(expectedValue, actualValue, EPSILON);
    }

    @Test
    public void testDoOptimize_maximize() {
        // Objective function: f(x, y) = -(x^2 + y^2)
        MultivariateFunction objective = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return -(x * x + y * y);
            }
        };

        // Create the optimizer
        double relativeThreshold = 1e-3;
        double absoluteThreshold = 1e-6;
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);

        // Set the objective function
        optimizer.setObjective(objective);

        // Set the start point
        double[] startPoint = {1, 2};
        optimizer.setStartPoint(startPoint);

        // Set the goal type
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        for (int i = 0; i < startPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], EPSILON);
        }
        assertEquals(expectedValue, actualValue, EPSILON);
    }
}