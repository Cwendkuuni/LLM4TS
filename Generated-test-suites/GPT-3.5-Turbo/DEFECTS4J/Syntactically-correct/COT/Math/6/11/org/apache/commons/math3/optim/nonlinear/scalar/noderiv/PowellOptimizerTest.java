
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 1.0, null);

        // Test case 1: Simple optimization with a known optimum
        // provide a test case where the objective function is a simple quadratic function
        // and the optimum is at (2, 3)
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return (x - 2) * (x - 2) + (y - 3) * (y - 3);
            }
        };

        double[] startPoint = {0, 0};
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        double[] expected = {2, 3};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);

        // Test case 2: Optimization with linear constraints
        // Add constraints that restrict the search space
        // x >= 0 and y >= 0
        final MultivariateFunction constrainedFunction = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return (x - 2) * (x - 2) + (y - 3) * (y - 3);
            }
        };

        final LinearConstraint xConstraint = new LinearConstraint(new double[]{-1, 0}, Relationship.GEQ, 0);
        final LinearConstraint yConstraint = new LinearConstraint(new double[]{0, -1}, Relationship.GEQ, 0);

        final ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(xConstraint);
        constraints.add(yConstraint);

        // Starting point outside the feasible region (x=-1, y=-1)
        startPoint = new double[]{-1, -1};
        result = optimizer.optimize(100, constrainedFunction, GoalType.MINIMIZE, startPoint, constraints);
        expected = new double[]{2, 3};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}

