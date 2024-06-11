
package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };

        double expectedMinValue = 0.5;
        double expectedMinX = 2.0;

        UnivariatePointValuePair result = optimizer.doOptimize(function, null, 0, 3);
        assertEquals(expectedMinValue, result.getValue(), EPSILON);
        assertEquals(expectedMinX, result.getPoint()[0], EPSILON);
    }
}
