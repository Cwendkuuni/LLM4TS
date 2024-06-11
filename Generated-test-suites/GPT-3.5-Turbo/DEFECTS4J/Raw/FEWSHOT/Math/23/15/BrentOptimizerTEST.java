package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

//import org.apache.commons.math3.exception.NumberIsTooSmallException;
//import org.apache.commons.math3.exception.NotStrictlyPositiveException;
//import org.apache.commons.math3.optimization.ConvergenceChecker;
//import org.apache.commons.math3.optimization.GoalType;
//import org.apache.commons.math3.util.FastMath;
//import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = new UnivariatePointValuePair(1.0, 2.0);
        // doOptimize() method is protected, so it's not directly testable.
        // We can create a child class BrentOptimizerTestChild, and override doOptimize()
        // to return specific result that we want to test.
        BrentOptimizer brentOptimizer = new BrentOptimizer(){
            @Override
            protected UnivariatePointValuePair doOptimize() {
                return result;
            }
        };

        // Check if the result returned by doOptimize() is the same that is returned by BrentOptimizer
        // This test doesn't check the actual working of BrentOptimizer, but rather the integrity between method calls.
        UnivariatePointValuePair actualResult = brentOptimizer.optimize();
        assertEquals(result, actualResult);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair expectedResult = a;

        // Check if the best() method is returning the correct result
        UnivariatePointValuePair actualResult = best(a, b, true);
        assertEquals(expectedResult, actualResult);
    }
}