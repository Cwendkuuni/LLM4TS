import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }
    
    @Test
    public void testDoOptimize_GoalType_Minimize_SuccessfulOptimization() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedOptimum = {-2.0, -1.0, 0.0};
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertArrayEquals(expectedOptimum, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_GoalType_Maximize_SuccessfulOptimization() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedOptimum = {3.0, 2.0, 1.0};
        
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertArrayEquals(expectedOptimum, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_CustomConvergenceChecker_SuccessfulOptimization() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedOptimum = {0.0, 0.0, 0.0};
        
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-9;
            }
        };
        optimizer.setConvergenceChecker(customChecker);
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertArrayEquals(expectedOptimum, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsThreshold_NegativeValue_ExceptionThrown() {
        new PowellOptimizer(1e-6, -1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelThreshold_TooSmallValue_ExceptionThrown() {
        new PowellOptimizer(1e-32, 1e-6);
    }
}