import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 1E-10);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 1.0e-14);
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 2.0, 1.5);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1E-10, 1E-14);
        assertEquals(0.0, result, 1E-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0, -1E-10, 1E-14);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1E-10, -1E-14);
    }

    // Add more test cases for other methods as needed

}