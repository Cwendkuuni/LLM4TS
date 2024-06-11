import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    private static final double ABSOLUTE_THRESHOLD = 1E-12;
    private static final double RELATIVE_THRESHOLD = 1E-10;

    private static final int MAX_ITERATIONS = 1000;

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        optimizer.setMaxIterations(MAX_ITERATIONS);
        double result = optimizer.optimize(1.0, x -> x*x).getPoint();
        assertEquals(0.0, result, ABSOLUTE_THRESHOLD);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD, (iteration, previous, current) -> {
            if (iteration >= MAX_ITERATIONS) {
                return true;
            }
            double previousValue = previous.getValue();
            double currentValue = current.getValue();
            double delta = Math.abs(currentValue - previousValue);
            double relativeError = delta / Math.max(Math.abs(previousValue), Math.abs(currentValue));
            return relativeError < RELATIVE_THRESHOLD && delta < ABSOLUTE_THRESHOLD;
        });
        double result = optimizer.optimize(1.0, x -> x*x).getPoint();
        assertEquals(0.0, result, ABSOLUTE_THRESHOLD);
    }
}