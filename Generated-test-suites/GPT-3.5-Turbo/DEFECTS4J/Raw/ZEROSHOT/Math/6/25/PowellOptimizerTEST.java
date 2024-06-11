import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    private static PowellOptimizer optimizer;

    @BeforeClass
    public static void setup() {
        optimizer = new PowellOptimizer(0.0001, 0.0001);
    }

    @Test
    public void testOptimize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(startPoint, goalType);
        double[] expectedPoint = {1.0, 1.0, 1.0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testOptimize_maximize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.optimize(startPoint, goalType);
        double[] expectedPoint = {-1.0, -2.0, -3.0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        assertEquals(expected, result);
    }

}