import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor_WithValidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_WithInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_WithInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
    }

    @Test
    public void testDoOptimize_Minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_Maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(10, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_InvalidMinAndMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(10);
        optimizer.setMax(0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNull(result);
    }

    @Test
    public void testDoOptimize_ConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iter >= 10;
            }
        });
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

}