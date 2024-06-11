import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double DEFAULT_RELATIVE_TOLERANCE = 1e-14;
    private static final double DEFAULT_ABSOLUTE_TOLERANCE = 1e-14;

    @Test
    public void testDoOptimizeMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int i, UnivariatePointValuePair a, UnivariatePointValuePair b) {
                return Math.abs(a.getValue() - b.getValue()) < 1e-4;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        Assert.assertEquals(0, result.getPoint(), 1e-4);
        Assert.assertEquals(0, result.getValue(), 1e-4);
    }

    @Test
    public void testDoOptimizeMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int i, UnivariatePointValuePair a, UnivariatePointValuePair b) {
                return Math.abs(a.getValue() - b.getValue()) < 1e-4;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        Assert.assertEquals(0, result.getPoint(), 1e-4);
        Assert.assertEquals(0, result.getValue(), 1e-4);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, -1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        new BrentOptimizer(1e-16, DEFAULT_ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 5);

        // Test minimization goal type
        UnivariatePointValuePair bestMin = optimizer.best(a, b, true);
        Assert.assertEquals(b, bestMin);

        // Test maximization goal type
        UnivariatePointValuePair bestMax = optimizer.best(a, b, false);
        Assert.assertEquals(a, bestMax);
    }

    @Test
    public void testBestBothNull() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair result = optimizer.best(null, null, true);
        Assert.assertNull(result);
    }

    @Test
    public void testBestBNull() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair result = optimizer.best(a, null, true);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testBestANull() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 5);
        UnivariatePointValuePair result = optimizer.best(null, b, true);
        Assert.assertEquals(b, result);
    }

    @Test
    public void testBestEqualValues() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        Assert.assertEquals(a, best);
    }

    @Test
    public void testBestEqualValuesMax() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair best = optimizer.best(a, b, false);
        Assert.assertEquals(a, best);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int i, UnivariatePointValuePair a, UnivariatePointValuePair b) {
                return Math.abs(a.getValue() - b.getValue()) < 1e-4;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        Assert.assertEquals(0, result.getPoint(), 1e-4);
        Assert.assertEquals(0, result.getValue(), 1e-4);
    }

    @Test
    public void testDoOptimizeWithAdditionalConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_TOLERANCE, DEFAULT_ABSOLUTE_TOLERANCE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int i, UnivariatePointValuePair a, UnivariatePointValuePair b) {
                return Math.abs(a.getValue() - b.getValue()) < 1e-4 || i > 100;
            }
        });
        UnivariatePointValuePair result = optimizer.optimize();
        Assert.assertEquals(0, result.getPoint(), 1e-4);
        Assert.assertEquals(0, result.getValue(), 1e-4);
    }

}