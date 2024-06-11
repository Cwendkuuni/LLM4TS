import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create the PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);

        // Set the goal type
        optimizer.withGoalType(GoalType.MINIMIZE);

        // Set the start point
        optimizer.withStartPoint(new double[] {1.0, 2.0});

        // Set the objective function
        optimizer.withObjectiveFunction(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        });

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        assertEquals(0.0, result.getPoint()[0], 1e-4);
        assertEquals(0.0, result.getPoint()[1], 1e-4);
        assertEquals(0.0, result.getValue(), 1e-4);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        // Create the PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Convergence condition
                return Math.abs(previous.getValue() - current.getValue()) < 1e-8;
            }
        });

        // Set the goal type
        optimizer.withGoalType(GoalType.MINIMIZE);

        // Set the start point
        optimizer.withStartPoint(new double[] {1.0, 2.0});

        // Set the objective function
        optimizer.withObjectiveFunction(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        });

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        assertEquals(0.0, result.getPoint()[0], 1e-4);
        assertEquals(0.0, result.getPoint()[1], 1e-4);
        assertEquals(0.0, result.getValue(), 1e-4);
    }

    @Test
    public void testDoOptimizeWithNegativeValues() {
        // Create the PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);

        // Set the goal type
        optimizer.withGoalType(GoalType.MINIMIZE);

        // Set the start point
        optimizer.withStartPoint(new double[] {-1.0, -2.0});

        // Set the objective function
        optimizer.withObjectiveFunction(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        });

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        assertEquals(0.0, result.getPoint()[0], 1e-4);
        assertEquals(0.0, result.getPoint()[1], 1e-4);
        assertEquals(0.0, result.getValue(), 1e-4);
    }

    @Test
    public void testDoOptimizeWithDefaultTolerance() {
        // Create the PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);

        // Set the goal type
        optimizer.withGoalType(GoalType.MINIMIZE);

        // Set the start point
        optimizer.withStartPoint(new double[] {1.0, 2.0});

        // Set the objective function
        optimizer.withObjectiveFunction(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        });

        // Optimize
        PointValuePair result = optimizer.optimize();

        // Check the result
        assertEquals(0.0, result.getPoint()[0], 1e-4);
        assertEquals(0.0, result.getPoint()[1], 1e-4);
        assertEquals(0.0, result.getValue(), 1e-4);
    }
}