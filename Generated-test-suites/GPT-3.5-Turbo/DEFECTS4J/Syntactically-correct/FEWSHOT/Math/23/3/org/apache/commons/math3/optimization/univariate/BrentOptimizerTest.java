
package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize_minimizeFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE);

        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximizeFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return -x * x + 4 * x - 3;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, f, GoalType.MAXIMIZE);

        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test(expected = TooManyEvaluationsException.class)
    public void testDoOptimize_maxEvaluationsExceeded() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        optimizer.optimize(10, f, GoalType.MINIMIZE);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidRelativeThreshold() {
        new BrentOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRelativeThresholdValue() {
        new BrentOptimizer(1e-15, 1e-6);
    }
    
    @Test
    public void testDoOptimize_zeroThreshold() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, 0, 0);

        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }
    
}
