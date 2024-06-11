package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private final double EPSILON = 1E-8;
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1E-4, 1E-4);
        
        //Test case 1
        UnivariateFunction f1 = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        UnivariatePointValuePair result1 = optimizer.optimize(f1, GoalType.MINIMIZE, 0, 2);
        assertEquals(1.0, result1.getPoint(), EPSILON);
        assertEquals(0.0, result1.getValue(), EPSILON);
        
        //Test case 2
        UnivariateFunction f2 = new UnivariateFunction() {
            public double value(double x) {
                return x * x * x - 5 * x * x + 3 * x;
            }
        };
        UnivariatePointValuePair result2 = optimizer.optimize(f2, GoalType.MAXIMIZE, -5, 5);
        assertEquals(-5.0, result2.getPoint(), EPSILON);
        assertEquals(50.0, result2.getValue(), EPSILON);
        
        //Test case 3
        UnivariateFunction f3 = new UnivariateFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        UnivariatePointValuePair result3 = optimizer.optimize(f3, GoalType.MINIMIZE, -10, 10);
        assertEquals(FastMath.PI, result3.getPoint(), EPSILON);
        assertEquals(-Math.PI, result3.getValue(), EPSILON);
    }
}