import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize_minimizeCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        double expected = -3.0;
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result.getValue(), 0.01);
        assertArrayEquals(startPoint, result.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimize_maximizeCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        double expected = 3.0;
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result.getValue(), 0.01);
        assertArrayEquals(startPoint, result.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimize_absoluteThresholdCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.1);

        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);

        optimizer.setGoalType(GoalType.MINIMIZE);
        
        double expected = -3.0;
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result.getValue(), 0.01);
        assertArrayEquals(startPoint, result.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimize_relativeThresholdCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);

        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);

        optimizer.setGoalType(GoalType.MINIMIZE);
        
        double expected = -3.0;
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result.getValue(), 0.01);
        assertArrayEquals(startPoint, result.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimize_customConvergenceCheckerCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01, new CustomConvergenceChecker());

        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);

        optimizer.setGoalType(GoalType.MINIMIZE);
        
        double expected = -3.0;
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result.getValue(), 0.01);
        assertArrayEquals(startPoint, result.getPoint(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
    }

    @Test(expected = TooManyEvaluationsException.class)
    public void testDoOptimize_tooManyEvaluations() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 1.0);

        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);

        optimizer.setGoalType(GoalType.MINIMIZE);
        
        optimizer.setMaxEvaluations(5);
        
        optimizer.doOptimize();
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return false; // Do not converge
        }
    }
}