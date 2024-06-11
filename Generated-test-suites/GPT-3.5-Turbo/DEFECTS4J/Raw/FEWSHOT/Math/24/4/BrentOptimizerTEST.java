import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BaseAbstractUnivariateOptimizer optimizer1 = new BrentOptimizer(1.0, 0.001);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        assertEquals(null, result1);

        // Test case 2
        double[] values2 = {1.0, 2.0, 3.0};
        double[] expectedResults2 = {1.0, 2.0, 3.0};
        BaseAbstractUnivariateOptimizer optimizer2 = new BrentOptimizer(1.0, 0.001);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        for (int i = 0; i < values2.length; i++) {
            assertEquals(expectedResults2[i], result2.getValue(), 0.001);
        }

        // Test case 3
        double[] values3 = {-1.0, -2.0, -3.0};
        double[] expectedResults3 = {-1.0, -2.0, -3.0};
        BaseAbstractUnivariateOptimizer optimizer3 = new BrentOptimizer(1.0, 0.001);
        UnivariatePointValuePair result3 = optimizer3.doOptimize();
        for (int i = 0; i < values3.length; i++) {
            assertEquals(expectedResults3[i], result3.getValue(), 0.001);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor_relLessThanMinRelativeTolerance() {
        BaseAbstractUnivariateOptimizer optimizer = new BrentOptimizer(0.5, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor_absLessOrEqualToZero() {
        BaseAbstractUnivariateOptimizer optimizer = new BrentOptimizer(1.0, 0);
    }
}