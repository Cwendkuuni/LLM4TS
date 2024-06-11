import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        MultivariateFunction function1 = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        PowellOptimizer optimizer1 = new PowellOptimizer(1e-6, 1e-6);
        optimizer1.setGoalType(GoalType.MINIMIZE);
        optimizer1.optimize(function1, GoalType.MINIMIZE, new double[]{1, 1});

        // Test case 2
        MultivariateFunction function2 = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6);
        optimizer2.setGoalType(GoalType.MINIMIZE);
        optimizer2.optimize(function2, GoalType.MINIMIZE, new double[]{1, 1, 1});
    }
}