import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_Minimization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimize_Maximization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(10.0, result.getPoint(), 0.01);
    }

    @Test
    public void testSetMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(1);
        Assert.assertEquals(1.0, optimizer.getMin(), Precision.EPSILON);
    }

    @Test
    public void testSetMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMax(10);
        Assert.assertEquals(10.0, optimizer.getMax(), Precision.EPSILON);
    }

    @Test
    public void testSetStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setStartValue(5);
        Assert.assertEquals(5.0, optimizer.getStartValue(), Precision.EPSILON);
    }

    @Test
    public void testSetGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        Assert.assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testConstructorWithRel() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        Assert.assertEquals(0.1, optimizer.getRelativeThreshold(), Precision.EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithRelNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.1, 0.01);
    }

    @Test
    public void testConstructorWithAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), Precision.EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01);
    }
}