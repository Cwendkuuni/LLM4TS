
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimizeMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Test basic functionality
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test edge cases
        optimizer.setMin(0);
        optimizer.setMax(1);
        optimizer.setStartValue(0);
        result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        optimizer.setMin(0);
        optimizer.setMax(1);
        optimizer.setStartValue(1);
        result = optimizer.doOptimize();
        assertEquals(1, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Test basic functionality
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(10, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test edge cases
        optimizer.setMin(0);
        optimizer.setMax(1);
        optimizer.setStartValue(0);
        result = optimizer.doOptimize();
        assertEquals(1, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        optimizer.setMin(0);
        optimizer.setMax(1);
        optimizer.setStartValue(1);
        result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-1e-6, 1e-6);
    }
}
