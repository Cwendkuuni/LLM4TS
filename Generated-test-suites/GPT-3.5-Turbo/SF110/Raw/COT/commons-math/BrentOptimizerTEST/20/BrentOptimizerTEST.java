import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.0001, 0.001);
    }

    @Test
    public void testConstructor_ValidArguments() {
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeToleranceTooSmall() {
        optimizer = new BrentOptimizer(0.00001, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteToleranceNotStrictlyPositive() {
        optimizer = new BrentOptimizer(0.0001, 0.0);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(0, Precision.compareTo(result.getPoint(), 0));
        Assert.assertEquals(0, Precision.compareTo(result.getValue(), 0));
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);

        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);

        Assert.assertSame(result1, a);
        Assert.assertSame(result2, b);
    }

    @Test
    public void testBest_Null() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);

        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);

        Assert.assertSame(result1, b);
        Assert.assertSame(result2, b);
    }

    @Test
    public void testBest_EqualValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 0);

        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);

        Assert.assertSame(result1, a);
        Assert.assertSame(result2, b);
    }

}