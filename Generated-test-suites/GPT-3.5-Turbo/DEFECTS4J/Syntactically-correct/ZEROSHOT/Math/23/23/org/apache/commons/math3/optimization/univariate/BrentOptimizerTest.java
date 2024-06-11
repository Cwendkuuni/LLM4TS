import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testOptimize() {
        double rel = 2 * FastMath.ulp(1d);
        double abs = 0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double min = 0;
        double max = 10;
        double startValue = 5;
        GoalType goalType = GoalType.MINIMIZE;

        optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        }, goalType, min, max, startValue);

        assertEquals(0, optimizer.getLastResult().getPoint(), EPSILON);
        assertEquals(0, optimizer.getLastResult().getValue(), EPSILON);
    }

    @Test
    public void testOptimizeUnboundedAbove() {
        double rel = 2 * FastMath.ulp(1d);
        double abs = 0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double min = 0;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 5;
        GoalType goalType = GoalType.MINIMIZE;

        optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        }, goalType, min, max, startValue);

        assertEquals(0, optimizer.getLastResult().getPoint(), EPSILON);
        assertEquals(0, optimizer.getLastResult().getValue(), EPSILON);
    }

    @Test
    public void testOptimizeUnboundedBelow() {
        double rel = 2 * FastMath.ulp(1d);
        double abs = 0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double min = Double.NEGATIVE_INFINITY;
        double max = 10;
        double startValue = 5;
        GoalType goalType = GoalType.MINIMIZE;

        optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        }, goalType, min, max, startValue);

        assertEquals(0, optimizer.getLastResult().getPoint(), EPSILON);
        assertEquals(0, optimizer.getLastResult().getValue(), EPSILON);
    }

    @Test
    public void testOptimizeGoalTypeMaximize() {
        double rel = 2 * FastMath.ulp(1d);
        double abs = 0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double min = 0;
        double max = 10;
        double startValue = 5;
        GoalType goalType = GoalType.MAXIMIZE;

        optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return -x * x;
            }
        }, goalType, min, max, startValue);

        assertEquals(10, optimizer.getLastResult().getPoint(), EPSILON);
        assertEquals(-100, optimizer.getLastResult().getValue(), EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeInvalidRelativeThreshold() {
        double rel = 0;
        double abs = 0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidAbsoluteThreshold() {
        double rel = 2 * FastMath.ulp(1d);
        double abs = -1;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        new BrentOptimizer(rel, abs, checker);
    }

}