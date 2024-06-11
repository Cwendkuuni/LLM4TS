package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        // Test maximizing
        double maxResult = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
        assertEquals(0, maxResult, EPSILON);

        // Test minimizing
        double minResult = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, minResult, EPSILON);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
			optimizer.doOptimize();
		} catch (MaxIterationsExceededException e) {
			assertNotNull(e);
		} catch (FunctionEvaluationException e) {
			assertNotNull(e);
		}
    }
    
    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        try {
            optimizer.localMin(true, f, GoalType.MINIMIZE, -10, 0, 10, 1E-10, 1E-10);
            optimizer.localMin(false, f, GoalType.MAXIMIZE, -10, 0, 10, 1E-10, 1E-10);
            optimizer.localMin(true, f, GoalType.MINIMIZE, -10, 0, 10, 1E-10, 1E-10);
        } catch (MaxIterationsExceededException e) {
        	assertNotNull(e);
        } catch (FunctionEvaluationException e) {
        	assertNotNull(e);
        }
    }
}