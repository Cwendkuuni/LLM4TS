import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.001;
        ConvergenceChecker checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertNotNull(result);
    }

    @Test
    public void testBest() {
        boolean isMinim = true;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        Assert.assertEquals(a, result);
    }

}