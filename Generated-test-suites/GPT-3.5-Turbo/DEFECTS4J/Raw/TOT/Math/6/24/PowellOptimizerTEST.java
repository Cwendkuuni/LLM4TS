import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallException() {
        new PowellOptimizer(0.5, 0, new SimpleValueChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        new PowellOptimizer(1, -1, new SimpleValueChecker());
    }

    @Test
    public void testDoOptimizeWithMinimizeGoalType() {
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return (Math.pow(point[0], 2) + Math.pow(point[1], 2))/2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        optimizer.setMaxEvaluations(1000);

        double[] startPoint = {1, 2};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(10, objectiveFunction, goalType, startPoint);
        
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;

        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-10);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimizeWithMaximizeGoalType() {
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return -(Math.pow(point[0], 2) + Math.pow(point[1], 2))/2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        optimizer.setMaxEvaluations(1000);

        double[] startPoint = {1, 2};
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.optimize(10, objectiveFunction, goalType, startPoint);
        
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;

        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-10);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-10);
    }

    @Test(expected = TooManyEvaluationsException.class)
    public void testDoOptimizeThrowsTooManyEvaluationsException() {
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return -(Math.pow(point[0], 2) + Math.pow(point[1], 2))/2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        optimizer.setMaxEvaluations(1);

        double[] startPoint = {1, 2};
        GoalType goalType = GoalType.MAXIMIZE;
        optimizer.optimize(10, objectiveFunction, goalType, startPoint);
    }

}