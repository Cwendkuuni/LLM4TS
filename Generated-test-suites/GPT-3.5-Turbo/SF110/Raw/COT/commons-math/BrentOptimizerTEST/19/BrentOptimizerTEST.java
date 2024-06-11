import org.junit.Test;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        double rel = 0.1;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = new SimpleValueChecker(0.01, 0.001);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        Assert.assertEquals(rel, optimizer.relativeThreshold, Precision.EPSILON);
        Assert.assertEquals(abs, optimizer.absoluteThreshold, Precision.EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor_withInvalidRel() {
        double rel = -0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor_withInvalidAbs() {
        double rel = 0.1;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize_withIncreasingMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, 0, 10);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(0.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }
    
    @Test
    public void testDoOptimize_withDecreasingMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                return -x;
            }
        }, GoalType.MINIMIZE, -10, 0);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(0.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize_withIncreasingMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                if (x > 5) {
                    return 0;
                } else {
                    return Math.pow(x - 5, 2);
                }
            }
        }, GoalType.MINIMIZE, 0, 10);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(5.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize_withDecreasingMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                if (x < -5) {
                    return 0;
                } else {
                    return Math.pow(x + 5, 2);
                }
            }
        }, GoalType.MINIMIZE, -10, 0);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(-5.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize_withConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, new SimpleValueChecker(0.01, 0.001));
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 5, 2);
            }
        }, GoalType.MINIMIZE, 0, 10);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(5.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize_withMaxIterations() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, new SimpleValueChecker(0.01, 0.001));
        optimizer.setMaxIterations(10);
        optimizer.minimize(new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 10, 2);
            }
        }, GoalType.MINIMIZE, 0, 10);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertEquals(10.0, result.getPoint(), Precision.EPSILON);
        Assert.assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

}