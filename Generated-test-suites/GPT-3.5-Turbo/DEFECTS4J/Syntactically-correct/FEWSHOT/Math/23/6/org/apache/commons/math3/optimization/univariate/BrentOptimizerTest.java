
package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private static final double EPSILON = 0.0000001;

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_ThrowsExceptionWhenRelativeThresholdIsTooSmall() {
        new BrentOptimizer(1e-10, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_ThrowsExceptionWhenAbsoluteThresholdIsNotPositive() {
        new BrentOptimizer(1.0, 0.0);
    }

    @Test
    public void testDoOptimize() {
        double[] values = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE,
                0.0, 6.0);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_FunctionWithMultipleMinima() {
        double[] values = { 1.0, 1.0, 3.0, 4.0, 5.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE,
                0.0, 6.0);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_FunctionWithMultipleMaxima() {
        double[] values = { 1.0, 2.0, 3.0, 3.0, 5.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MAXIMIZE,
                0.0, 6.0);
        
        assertNotNull(result);
        assertEquals(3.0, result.getPoint(), EPSILON);
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_SingleValueOptimization() {
        double[] values = { 1.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE,
                0.0, 2.0);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_UnboundedOptimization() {
        double[] values = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_SingularFunction() {
        double[] values = { 1.0, 1.0, 1.0, 1.0, 1.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE,
                0.0, 2.0);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_NoOptimizationRange() {
        double[] values = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        TestFunction func = new TestFunction(values);
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        UnivariatePointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE,
                6.0, 7.0);
        
        assertNull(result);
    }

    private static class TestFunction implements UnivariateFunction {

        private double[] values;

        public TestFunction(double[] values) {
            this.values = values;
        }

        @Override
        public double value(double x) {
            return values[(int) x];
        }
    }
}
