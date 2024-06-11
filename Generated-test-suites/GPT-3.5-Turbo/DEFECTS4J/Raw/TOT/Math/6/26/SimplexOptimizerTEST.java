import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private MultivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
        function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
    }

    @Test
    public void testOptimize_Minimize() {
        double[] startPoint = { 1, 2 };
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
    }
    
    @Test
    public void testOptimize_Maximize() {
        double[] startPoint = { 1, 2 };
        PointValuePair result = optimizer.optimize(100, function, GoalType.MAXIMIZE, startPoint);
        assertEquals(0.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
    }
    
    @Test
    public void testOptimize_EdgeCases() {
        double[] startPoint = { Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY };
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        assertEquals(Double.NEGATIVE_INFINITY, result.getPoint()[0], 1e-5);
        assertEquals(Double.POSITIVE_INFINITY, result.getPoint()[1], 1e-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_NullFunction() {
        double[] startPoint = { 1, 2 };
        optimizer.optimize(100, null, GoalType.MINIMIZE, startPoint);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_NullStartPoint() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, null);
    }
}