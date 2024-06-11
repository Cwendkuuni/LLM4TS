import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.0001, 0.001);
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0001, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test (expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        optimizer = new BrentOptimizer(-0.0001, 0.001);
    }

    @Test (expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.0001, 0.0);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0001);
        // Add more assertions for the calculated value

    }

    // Add more test cases for other methods
    
}