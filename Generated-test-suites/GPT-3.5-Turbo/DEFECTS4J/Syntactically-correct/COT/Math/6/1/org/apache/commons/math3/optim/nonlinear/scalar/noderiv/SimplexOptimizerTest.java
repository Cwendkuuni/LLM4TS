
import org.junit.*;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal(){
        double[] startPoint = {1.0, 2.0, 3.0};
        double rel = 1e-10;
        double abs = 1e-10;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);

        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1] + point[2] * point[2];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, startPoint);
        
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double expectedValue = 0.0;

        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        assertEquals(expectedValue, result.getValue(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

}
