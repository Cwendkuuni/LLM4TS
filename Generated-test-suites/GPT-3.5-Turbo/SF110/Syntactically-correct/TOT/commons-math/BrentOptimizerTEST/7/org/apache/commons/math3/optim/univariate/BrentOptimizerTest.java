import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor_WithValidArguments() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.relativeThreshold, 0.0001);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0001);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_WithInvalidRelThreshold() {
        double rel = -0.001;
        double abs = 0.01;
        new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_WithInvalidAbsThreshold() {
        double rel = 0.001;
        double abs = 0.0;
        new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimize_WithMinimizationGoal() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        double min = -5.0;
        double max = 5.0;
        UnivariatePointValuePair result = optimizer.optimize(100, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, min, max);
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_WithMaximizationGoal() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        double min = 0.0;
        double max = 10.0;
        UnivariatePointValuePair result = optimizer.optimize(100, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return -x * x;
            }
        }, GoalType.MAXIMIZE, min, max);
        
        assertNotNull(result);
        assertEquals(10.0, result.getPoint(), 0.0001);
        assertEquals(-100.0, result.getValue(), 0.0001);
    }
}