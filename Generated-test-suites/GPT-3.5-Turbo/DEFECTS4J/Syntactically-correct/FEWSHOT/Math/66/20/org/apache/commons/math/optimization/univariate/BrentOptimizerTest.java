import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testOptimizeStartValue() throws FunctionEvaluationException, MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        TestFunction f = new TestFunction();

        // Test with startValue within the interval
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 2, 4, 3.5);
        assertEquals(2.0, result, 0);

        // Test with startValue outside the interval
        result = optimizer.optimize(f, GoalType.MINIMIZE, 2, 4, 1.5);
        assertEquals(2.0, result, 0);
    }

    @Test
    public void testOptimizeBest() throws FunctionEvaluationException, MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        TestFunction f = new TestFunction();

        // Test with startValue within the interval
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 2, 4);
        assertEquals(2.0, result, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidEps() throws FunctionEvaluationException, MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        TestFunction f = new TestFunction();
        optimizer.localMin(true, f, GoalType.MINIMIZE, 2, 3, 4, 0, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidT() throws FunctionEvaluationException, MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        TestFunction f = new TestFunction();
        optimizer.localMin(true, f, GoalType.MINIMIZE, 2, 3, 4, 1, 0);
    }

    @Test
    public void testLocalMin() throws FunctionEvaluationException, MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();
        TestFunction f = new TestFunction();
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 2, 3, 4, 1E-6, 1E-10);
        assertEquals(2.0, result, 0);
    }
    
    class TestFunction implements UnivariateRealFunction {

        public double value(double x) throws FunctionEvaluationException {
            if (x == 2) {
                return 0;
            } else {
                return x - 2;
            }
        }
    }
}