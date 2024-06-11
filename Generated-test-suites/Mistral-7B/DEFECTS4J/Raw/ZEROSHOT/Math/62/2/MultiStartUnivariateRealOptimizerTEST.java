import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws FunctionEvaluationException, ConvergenceException, MathIllegalStateException {
        // Test with a simple function
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchUnivariateRealOptimizer<>(testFunction), 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());

        // Test with minimum goal
        PointValuePair optimalPoint1 = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 10.0);
        assertEquals(2.0, optimalPoint1.getPoint().doubleValue(), 0.01);
        assertArrayEquals(new UnivariateRealPointValuePair[]{
                new UnivariateRealPointValuePair(2.0, 2.0),
                new UnivariateRealPointValuePair(4.161468373754475, 5.341314555956746),
                new UnivariateRealPointValuePair(6.888888888888888, 3.168828612102548),
                new UnivariateRealPointValuePair(8.938271614456244, 1.416146836572426),
                new UnivariateRealPointValuePair(2.0, 2.0)}, optimizer.getOptima());

        // Test with maximum goal
        PointValuePair optimalPoint2 = optimizer.optimize(testFunction, GoalType.MAXIMIZE, 0.0, 10.0);
        assertEquals(8.0, optimalPoint2.getPoint().doubleValue(), 0.01);
        assertArrayEquals(new UnivariateRealPointValuePair[]{
                new UnivariateRealPointValuePair(8.0, 8.0),
                new UnivariateRealPointValuePair(5.434562548125732, 6.565432156786536),
                new UnivariateRealPointValuePair(3.121315789453121, 7.878787878787879),
                new UnivariateRealPointValuePair(1.213141592653589, 9.787878787878788),
                new UnivariateRealPointValuePair(8.0, 8.0)}, optimizer.getOptima());

        // Test with a non-converging function
        UnivariateRealFunction nonConvergingFunction = new NonConvergingFunction();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> nonConvergingOptimizer = new MultiStartUnivariateRealOptimizer<>(
                new BracketFindingMinimizer(new BracketingMethod(nonConvergingFunction, 0.0, 1.0, 0.001, 0.01, 100), 0.001, 0.01), 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        try {
            nonConvergingOptimizer.optimize(nonConvergingFunction, GoalType.MINIMIZE, 0.0, 1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // Expected
        }
        assertArrayEquals(new UnivariateRealPointValuePair[5], nonConvergingOptimizer.getOptima());
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4.0 * x + 4.0;
        }

        @Override
        public double derive(double x) {
            return 2.0 * x - 4.0;
        }
    }

    private static class NonConvergingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new IllegalStateException("Non-converging function");
        }

        @Override
        public double derive(double x) {
            throw new IllegalStateException("Non-converging function");
        }
    }
}