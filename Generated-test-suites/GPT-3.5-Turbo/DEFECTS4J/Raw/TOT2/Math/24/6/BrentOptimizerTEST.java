import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relativeThreshold = 0.001;
        double absoluteThreshold = 0.01;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);

        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorRelException() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relativeThreshold = 0.0001;
        double absoluteThreshold = 0.01;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorAbsException() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relativeThreshold = 0.001;
        double absoluteThreshold = -0.01;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
    }

    // Add more test cases for other methods as needed
}