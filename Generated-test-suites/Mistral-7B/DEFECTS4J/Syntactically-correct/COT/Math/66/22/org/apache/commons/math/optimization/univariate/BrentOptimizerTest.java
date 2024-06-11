import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Tests for BrentOptimizer class.
 */
public class BrentOptimizerTest {
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    /**
     * Test doOptimize method with exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    /**
     * Test optimize method with valid arguments.
     */
    @Test
    public void testOptimizeValid() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(2, -1);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(-2.0, result.getPoint(), 0);
        assertEquals(0.000251742381791, result.getValue(), 0.00025);
    }

    /**
     * Test optimize method with invalid lower bound.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidLowerBound() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(2, -1);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 0);
    }

    /**
     * Test optimize method with invalid function.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeInvalidFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return f.value(x);
            }
        }, GoalType.MINIMIZE, -10, 10, 0);
    }

    /**
     * Test optimize method with exception.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(0);
        UnivariateRealFunction f = new PolynomialFunction(2, -1);
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
    }
}