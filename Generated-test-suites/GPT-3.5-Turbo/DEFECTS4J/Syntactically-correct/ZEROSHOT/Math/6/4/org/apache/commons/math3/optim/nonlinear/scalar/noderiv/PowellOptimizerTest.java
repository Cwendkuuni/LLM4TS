import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {
    
    private PowellOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.0001, 0.0001);
    }
    
    @Test
    public void testDoOptimizeWithMinimizeGoal() {
        double[] startPoint = {1, 1};
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
        
        PointValuePair result = optimizer.getOptimum();
        double[] point = result.getPoint();
        double value = result.getValue();
        
        double[] expectedPoint = {1, 1};
        double expectedValue = 0;
        
        assertArrayEquals(expectedPoint, point, 0.0001);
        assertEquals(expectedValue, value, 0.0001);
    }
    
    @Test
    public void testDoOptimizeWithMaximizeGoal() {
        double[] startPoint = {1, 1};
        
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
        
        PointValuePair result = optimizer.getOptimum();
        double[] point = result.getPoint();
        double value = result.getValue();
        
        double[] expectedPoint = {1, 1};
        double expectedValue = 0;
        
        assertArrayEquals(expectedPoint, point, 0.0001);
        assertEquals(expectedValue, value, 0.0001);
    }
}