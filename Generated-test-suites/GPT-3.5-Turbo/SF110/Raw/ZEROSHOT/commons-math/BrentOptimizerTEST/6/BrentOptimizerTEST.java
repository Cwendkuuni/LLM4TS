import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        assertEquals(0.001, optimizer.relativeThreshold, 0.0);
        assertEquals(0.001, optimizer.absoluteThreshold, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.001, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert expected result
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);

        // Case for minimization
        boolean isMinim = true;
        UnivariatePointValuePair best = optimizer.best(a, b, isMinim);
        assertEquals(a, best);

        // Case for maximization
        isMinim = false;
        best = optimizer.best(a, b, isMinim);
        assertEquals(b, best);
    }
}