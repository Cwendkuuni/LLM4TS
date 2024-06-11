import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.001, 0.001, new SimpleValueChecker());
    }
    
    @Test
    public void testDoOptimize_MinimizeValue() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimize_MaximizeValue() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(10.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdTooSmall() {
        BrentOptimizer invalidOptimizer = new BrentOptimizer(0.0001, 0.001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdNotStrictlyPositive() {
        BrentOptimizer invalidOptimizer = new BrentOptimizer(0.001, -0.001);
    }
}