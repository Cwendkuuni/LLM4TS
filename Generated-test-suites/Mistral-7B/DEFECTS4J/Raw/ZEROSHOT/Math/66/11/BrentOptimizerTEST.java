import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NoBracketException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.Value;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimizeMinimum() throws Exception {
        UnivariateRealFunction function = new SimpleFunction(-Math.pow(x, 2));
        Double min = optimizer.optimize(function, GoalType.MINIMUM, -3.0, 3.0).getPoint();
        assertEquals(0.0, min, 1e-6);
    }

    @Test
    public void testOptimizeMaximum() throws Exception {
        UnivariateRealFunction function = new SimpleFunction(Math.pow(x, 2));
        Double max = optimizer.optimize(function, GoalType.MAXIMUM, -3.0, 3.0).getPoint();
        assertEquals(9.0, max, 1e-6);
    }

    @Test
    public void testOptimizeMinimumOutOfBounds() throws Exception {
        UnivariateRealFunction function = new SimpleFunction(Math.log(Math.abs(x) + 1));
        assertThrows(NoBracketException.class, () -> optimizer.optimize(function, GoalType.MINIMUM, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    @Test
    public void testOptimizeMaximumOutOfBounds() throws Exception {
        UnivariateRealFunction function = new SimpleFunction(Math.log(Math.abs(x) - 1));
        assertThrows(NoBracketException.class, () -> optimizer.optimize(function, GoalType.MAXIMUM, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    @Test
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new SimpleFunction(x -> Math.sin(Math.PI * x) / x);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMUM, bounds.getMin(), bounds.getMax()));
    }

    private static class SimpleFunction implements UnivariateRealFunction {
        private final DoubleFunction function;

        public SimpleFunction(DoubleFunction f) {
            function = f;
        }

        @Override
        public double value(double x) {
            return function.value(x);
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }

        public interface DoubleFunction {
            double value(double x);
        }
    }
}