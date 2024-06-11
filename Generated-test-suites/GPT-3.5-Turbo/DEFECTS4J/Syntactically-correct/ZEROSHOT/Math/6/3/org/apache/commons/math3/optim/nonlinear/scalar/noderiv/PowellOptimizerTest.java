import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.analysis.MultivariateFunction;

public class PowellOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsZero() {
        new PowellOptimizer(0.5, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelTooSmall() {
        new PowellOptimizer(1e-16, 0.5);
    }

    @Test
    public void testConstructorWithValidParameters() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimizeWithGoalTypeMinimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        double result = optimizer.doOptimize();
        assertEquals(-10.0, result, 0.01);
    }

    @Test
    public void testDoOptimizeWithGoalTypeMaximize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        double result = optimizer.doOptimize();
        assertEquals(10.0, result, 0.01);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        double[] startPoint = {1, 2, 3};
        double[] direction = {1, 1, 1};
        double alpha = 0.5;
        double[][] result = optimizer.newPointAndDirection(startPoint, direction, alpha);
        double[] newPoint = result[0];
        double[] newDirection = result[1];
        assertArrayEquals(new double[]{1.5, 2.5, 3.5}, newPoint, 0.01);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5}, newDirection, 0.01);
    }

    @Test
    public void testSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        double[] startPoint = {1, 2, 3};
        double[] direction = {1, 1, 1};
        UnivariatePointValuePair result = optimizer.search(startPoint, direction);
        assertEquals(4.0, result.getValue(), 0.01);
        assertEquals(1.0, result.getPoint(), 0.01);
    }

}