import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        
        PointValuePair result = optimizer.optimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        
        PointValuePair result = optimizer.optimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        double[] point = new double[]{1.0, 2.0, 3.0};
        double[] direction = new double[]{0.1, 0.2, 0.3};
        
        double[][] result = optimizer.newPointAndDirection(point, direction, 0.5);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new double[]{1.05, 2.1, 3.15}, result[0], 1e-6);
        assertArrayEquals(new double[]{0.05, 0.1, 0.15}, result[1], 1e-6);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        double[] point = new double[]{1.0, 2.0, 3.0};
        double[] direction = new double[]{0.1, 0.2, 0.3};
        
        UnivariatePointValuePair result = optimizer.line.search(point, direction);
        
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new PowellOptimizer(1e-15, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new PowellOptimizer(1e-6, 0);
    }

    @Test
    public void testComputeObjectiveValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        double[] point = new double[]{1.0, 2.0, 3.0};
        double value = optimizer.computeObjectiveValue(point);
        
        assertEquals(0.0, value, 1e-6);
    }

    // Add more test cases for edge cases and error scenarios as needed

}