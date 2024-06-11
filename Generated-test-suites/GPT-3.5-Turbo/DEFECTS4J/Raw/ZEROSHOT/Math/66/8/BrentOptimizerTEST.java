import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 6.28);
        assertEquals(1.570796, result, 0.000001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testOptimizeWithInvalidGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        optimizer.optimize(function, null, 0.0, 6.28);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        optimizer.setMaximalIterationCount(5);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 6.28);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        optimizer.setRelativeAccuracy(-1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 6.28);
    }

    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.cos(x);
            }
        };
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 6.28);
        assertEquals(0, result, 0.000001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeUnsupportedOperation() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.cos(x);
            }
        };
        optimizer.doOptimize();
    }

}