import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.001, 0.01);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_MaximumGreaterThanMinimum() {
        optimizer.setMin(10);
        optimizer.setMax(0);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdLessThanMinimumValue() {
        new BrentOptimizer(0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdNotPositive() {
        new BrentOptimizer(0.001, -0.01);
    }

}