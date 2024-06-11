import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertTrue(Precision.equals(0.0, result.getPoint(), 1e-6));
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeTolerance() {
        new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.1, -0.01);
    }

}