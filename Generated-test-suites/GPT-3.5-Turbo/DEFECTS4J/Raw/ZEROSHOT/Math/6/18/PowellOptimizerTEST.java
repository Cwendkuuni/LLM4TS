import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.1);
        double[] startPoint = {0.0, 0.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.doOptimize();

        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        PointValuePair expectedResult = new PointValuePair(expectedPoint, expectedValue);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.1);
        double[] p = {0.0, 0.0};
        double[] d = {1.0, 1.0};
        double optimum = 0.5;

        double[][] result = optimizer.newPointAndDirection(p, d, optimum);

        double[] expectedPoint = {0.5, 0.5};
        double[] expectedDirection = {0.5, 0.5};
        double[][] expected = {expectedPoint, expectedDirection};

        Assert.assertArrayEquals(expected, result);
    }
}