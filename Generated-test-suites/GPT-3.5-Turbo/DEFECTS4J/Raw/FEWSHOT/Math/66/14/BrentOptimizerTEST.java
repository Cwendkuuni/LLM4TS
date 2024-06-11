
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double minimum = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(2.0, minimum, 0.001);

        double maximum = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
        assertEquals(-2.0, maximum, 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };

        optimizer.setMaximalIterationCount(50);
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    @Test
    public void testOptimizeWithNullFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        double optimum = optimizer.optimize(null, GoalType.MINIMIZE, -10, 10);
        assertEquals(Double.NaN, optimum, 0.001);
    }

    @Test
    public void testOptimizeWithLoGreaterThanHi() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        double minimum = optimizer.optimize(f, GoalType.MINIMIZE, 10, -10);
        assertEquals(-10.0, minimum, 0.001);

        double maximum = optimizer.optimize(f, GoalType.MAXIMIZE, 10, -10);
        assertEquals(10.0, maximum, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 1, -1);
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        double optimum = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(Double.NaN, optimum, 0.001);
    }

    @Test
    public void testOptimizeWithGoalTypeMaximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double maximum = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
        assertEquals(0.0, maximum, 0.001);
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double minimum = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 3);
        assertEquals(2.0, minimum, 0.001);
    }
}
