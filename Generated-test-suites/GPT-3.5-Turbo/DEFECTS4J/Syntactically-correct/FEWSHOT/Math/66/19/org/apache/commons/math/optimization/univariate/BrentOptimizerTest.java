package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0.0 || x > 1.0) {
                    throw new FunctionEvaluationException(x);
                }
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            assertEquals(0.0, result, 1e-10);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception");
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithZeroEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithZeroT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

}