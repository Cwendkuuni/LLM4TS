import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_WhenMinimizingFunction_ReturnsCorrectOptimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Sin();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        double result = optimizer.optimize(f, goalType, min, max);
        double expected = 0;

        assertEquals(expected, result, 1E-10);
    }

    @Test
    public void testOptimize_WhenMaximizingFunction_ReturnsCorrectOptimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Sin();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        double result = optimizer.optimize(f, goalType, min, max);
        double expected = Math.PI;

        assertEquals(expected, result, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WhenEpsIsNotStrictlyPositive_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Sin();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        double eps = -1.0;
        optimizer.setRelativeAccuracy(eps);
        optimizer.optimize(f, goalType, min, max);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WhenTIsNotStrictlyPositive_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Sin();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        double t = -1.0;
        optimizer.setAbsoluteAccuracy(t);
        optimizer.optimize(f, goalType, min, max);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_WhenMaxIterationsExceeded_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new Sin();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(f, goalType, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_WhenFunctionEvaluationExceptionOccurs_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        optimizer.optimize(f, goalType, min, max);
    }

}