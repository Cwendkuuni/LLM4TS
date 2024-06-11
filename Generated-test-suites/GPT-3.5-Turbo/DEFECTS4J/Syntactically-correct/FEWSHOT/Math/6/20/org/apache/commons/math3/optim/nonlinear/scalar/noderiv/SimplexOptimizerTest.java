import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;

public class SimplexOptimizerTest {
    @Test
    public void testOptimizeInternal() {
        // Create an instance of the SimplexOptimizer class
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Define the objective function
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        // Set the optimization data
        OptimizationData[] optData = new OptimizationData[2];
        optData[0] = new org.apache.commons.math3.optimization.InitialGuess(new double[] {1, 1});
        optData[1] = new AbstractSimplex();

        // Optimize the objective function
        PointValuePair result = optimizer.optimizeInternal(100, f, GoalType.MINIMIZE, optData);

        // Verify the result
        assertEquals(0.0, result.getValue(), 1e-8);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getPoint()[1], 1e-8);
    }
}