import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class PowellOptimizerTest {

    @Test
    public void testConstructor1() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor2() {
        try {
            PowellOptimizer optimizer = new PowellOptimizer(-0.5, 0.0001);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructor3() {
        try {
            PowellOptimizer optimizer = new PowellOptimizer(0.0001, -0.5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e){
            // Expected exception
        }
    }

    @Test
    public void testOptimize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.optimize();
        assertNotNull(result);
    }

    @Test
    public void testOptimizeWithEmptyStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        try {
            PointValuePair result = optimizer.optimize();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}