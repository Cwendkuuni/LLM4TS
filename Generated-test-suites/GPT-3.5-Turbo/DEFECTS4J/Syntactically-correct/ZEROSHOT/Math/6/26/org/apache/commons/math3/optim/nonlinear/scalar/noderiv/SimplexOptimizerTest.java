import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    public SimplexOptimizerTest() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testSetSimplex() {
        // Create a Simplex
        AbstractSimplex simplex = new AbstractSimplex();
        // Set the Simplex
        optimizer.setSimplex(simplex);
        // Check if the Simplex was set correctly
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        // Create the objective function
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        // Set the start point
        double[] startPoint = {0.0, 0.0};
        // Set the goal type
        GoalType goalType = GoalType.MINIMIZE;
        // Set the optimization data
        OptimizationData[] optData = new OptimizationData[2];
        optData[0] = new AbstractSimplex();
        optData[1] = new InitialGuess(startPoint);
        // Optimize the objective function
        PointValuePair result = optimizer.optimizeInternal(1000, f, goalType, optData);
        // Check if the result is correct
        double[] expected = {0.0, 0.0};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize() {
        // Create the objective function
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        // Set the start point
        double[] startPoint = {0.0, 0.0};
        // Set the goal type
        GoalType goalType = GoalType.MINIMIZE;
        // Set the optimization data
        OptimizationData[] optData = new OptimizationData[2];
        optData[0] = new AbstractSimplex();
        optData[1] = new InitialGuess(startPoint);
        // Optimize the objective function
        PointValuePair result = optimizer.doOptimize();
        // Check if the result is correct
        double[] expected = {0.0, 0.0};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

}