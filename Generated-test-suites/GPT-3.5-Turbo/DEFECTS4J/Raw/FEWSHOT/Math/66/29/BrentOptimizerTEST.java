package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testDoOptimize() {
        try {
            optimizer.doOptimize();
            fail("MaxIterationsExceededException was not thrown");
        } catch (MaxIterationsExceededException e) {
            // expected behavior
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testOptimize_WithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 5.0, 2.0);
            assertEquals(1.0, result, 1E-10);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testOptimize_WithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
            assertEquals(1.0, result, 1E-10);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testOptimize_NotStrictlyPositiveException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 5.0, -2.0);
            fail("NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected behavior
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}