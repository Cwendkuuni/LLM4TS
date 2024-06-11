import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        double rel = 1e-6;
        double abs = -1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRel() {
        double rel = 1e-20;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 1);
        UnivariatePointValuePair result = BrentOptimizer.best(a, b, true);
        Assert.assertEquals(result, a);
    }
}