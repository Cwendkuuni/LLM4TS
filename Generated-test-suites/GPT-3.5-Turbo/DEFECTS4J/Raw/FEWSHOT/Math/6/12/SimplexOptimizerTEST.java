import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    private SimplexOptimizer optimizer;
    private double[] startPoint;
    private MultivariateFunction objectiveFunction;
    private GoalType goalType;
    private OptimizationData[] optimizationData;
    
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
        startPoint = new double[] { 0.0, 0.0 };
        objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        goalType = GoalType.MINIMIZE;
        optimizationData = null;
    }
    
    @Test
    public void testOptimizeInternal() {
        PointValuePair result = optimizer.optimizeInternal(100, objectiveFunction, goalType, optimizationData);
        assertNotNull(result);
        assertEquals(0.0, result.getPointRef()[0], 1e-10);
        assertEquals(0.0, result.getPointRef()[1], 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);
    }
}