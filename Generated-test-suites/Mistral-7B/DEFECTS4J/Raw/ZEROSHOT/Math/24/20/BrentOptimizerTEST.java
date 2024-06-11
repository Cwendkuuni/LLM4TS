import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private FunctionUnderTest function;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        function = new FunctionUnderTest();
    }

    @Test
    public void testBrentOptimizer_min_in_interval() throws Exception {
        final double minimum = -3.0;
        final double intervalLowerBound = -5.0;
        final double intervalUpperBound = 5.0;

        optimizer.optimize(function, intervalLowerBound, intervalUpperBound, null);

        PointValuePair result = optimizer.getResult();
        Assert.assertNotNull(result);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testBrentOptimizer_max_eval_exceeded() throws Exception {
        final double intervalLowerBound = -5.0;
        final double intervalUpperBound = 5.0;
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, intervalLowerBound, intervalUpperBound, null);
    }

    @Test
    public void testBrentOptimizer_min_not_in_interval() throws Exception {
        final double minimum = -4.0;
        final double intervalLowerBound = -5.0;
        final double intervalUpperBound = 3.0;

        optimizer.optimize(function, intervalLowerBound, intervalUpperBound, null);

        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertFalse(Precision.equals(minimum, result.getPoint(), 1e-6));
        assertNull(result.getValue());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_relative_threshold_too_small() throws Exception {
        final double relativeThreshold = 1e-9;
        final double absoluteThreshold = 1e-6;
        optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_absolute_threshold_negative() throws Exception {
        final double relativeThreshold = 1e-6;
        final double absoluteThreshold = -1e-6;
        optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }

    @Test
    public void testBrentOptimizer_convergence_checker() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 0.0;
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        optimizer.optimize(function, -10.0, 10.0, null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertTrue(result.getValue() < 0.0);
    }

    @Test
    public void testBrentOptimizer_convergence_criteria() throws Exception {
        optimizer.setMaxIterations(100);
        optimizer.optimize(function, -10.0, 10.0, null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
    }

    private static class FunctionUnderTest implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * Math.pow(x, 2) + Math.pow(x, 0.5) - 1;
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }
}