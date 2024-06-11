import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 20);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRel() {
        BrentOptimizer optimizer = new BrentOptimizer(0.000001, 0.0001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, -0.0001);
    }

}