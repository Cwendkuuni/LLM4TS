package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x + x - 6;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            Assert.assertEquals(-3, result, 0.01);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x + x - 6;
            }
        };
        try {
            double result = optimizer.localMin(false, f, GoalType.MINIMIZE, 0, 1, 10, 0.01, 0.01);
            Assert.assertEquals(0, result, 0.01);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }
}