import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithRelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorWithNegativeAbs() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorWithRelTooSmall() {
        new BrentOptimizer(0.0001, 0.001, null);
    }

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getRelativeThreshold());
        assertNull(optimizer.getAbsoluteThreshold());
    }

    @Test
    public void testDoOptimizeWithMinFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        UnivariateOptimizerResult result = optimizer.optimize(function, new InitialGuess(1.0), new SimpleBounds(0.0, 5.0), GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint().getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeWithMaxFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        UnivariateOptimizerResult result = optimizer.optimize(function, new InitialGuess(1.0), new SimpleBounds(0.0, 5.0), GoalType.MAXIMIZE);
        assertEquals(5.0, result.getPoint().getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeWithExtrapolation() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.SinFunction();
        UnivariateOptimizerResult result = optimizer.optimize(function, new InitialGuess(0.5), new SimpleBounds(-10.0, 10.0), GoalType.MINIMIZE);
        double expected = Math.PI / 2;
        assertEquals(expected, result.getPoint().getValue(), 0.0001);
        // Test exception when x is too close to a or b
        try {
            optimizer.optimize(function, new InitialGuess(expected - 0.001), new SimpleBounds(-10.0, 10.0), GoalType.MINIMIZE);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    public void testConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyConvergenceChecker());
        UnivariateFunction function = new Math.SinFunction();
        optimizer.optimize(function, new InitialGuess(0.5), new SimpleBounds(-10.0, 10.0), GoalType.MINIMIZE);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double error = current.getValue() - previous.getValue();
            return Math.abs(error) < 0.001;
        }
    }
}