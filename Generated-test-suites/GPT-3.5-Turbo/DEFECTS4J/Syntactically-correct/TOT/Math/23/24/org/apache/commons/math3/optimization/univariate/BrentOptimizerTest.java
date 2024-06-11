
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor_withValidArguments() {
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        Assert.assertEquals(rel, optimizer.relativeThreshold, 0.0);
        Assert.assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withInvalidRelThrowException() {
        double rel = 1e-16;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidAbsThrowException() {
        double rel = 1e-6;
        double abs = -1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testConstructor_withValidArguments_noChecker() {
        double rel = 1e-6;
        double abs = 1e-6;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        Assert.assertEquals(rel, optimizer.relativeThreshold, 0.0);
        Assert.assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test
    public void testDoOptimize_withMinimizationGoalTypeAndValidInput() {
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertTrue(result.getValue() >= 0);
    }

    @Test
    public void testDoOptimize_withMaximizationGoalTypeAndValidInput() {
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertTrue(result.getValue() <= 0);
    }

    @Test
    public void testDoOptimize_withConvergenceCheckerAndValidInput() {
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return iteration >= 100;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertTrue(result.getValue() >= 0);
    }
}

