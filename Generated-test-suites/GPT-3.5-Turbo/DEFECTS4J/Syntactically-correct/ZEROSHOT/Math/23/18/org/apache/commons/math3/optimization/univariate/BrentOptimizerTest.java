import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double result = optimizer.doOptimize();
        double expectedResult = 0.0;

        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.8);

        UnivariatePointValuePair result = best(a, b, true);

        Assert.assertEquals(a, result);
    }
    
    @Test
    public void testBest_BothNull() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;

        UnivariatePointValuePair result = best(a, b, true);

        Assert.assertNull(result);
    }
    
    @Test
    public void testBest_SameValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);

        UnivariatePointValuePair result = best(a, b, true);

        Assert.assertEquals(a, result);
    }
}