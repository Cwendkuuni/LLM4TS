import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Test the optimization function with typical input values
        UnivariatePointValuePair result = optimizer.optimize(100, new BrentOptimizer.UnivariateObjectiveFunction());
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);

        // Test the optimization function with edge case inputs
        result = optimizer.optimize(1, new BrentOptimizer.UnivariateObjectiveFunction());
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
    }
}