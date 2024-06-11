import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {

    /*  */
    @Test
    public void testDoOptimize() {
        // Given
        double[] startPoint = {1, 1};
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10, checker);
        optimizer.setMaxEvaluations(1000);
        
        // When
        PointValuePair result = optimizer.optimize(1000, objectiveFunction, GoalType.MINIMIZE, startPoint);

        // Then
        double expected = 0.0;
        double actual = result.getValue();
        Assert.assertEquals(expected, actual, 1e-10);
    }
    /*  */

    /*  */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        // Given
        double relativeThreshold = 0.0;
        double absoluteThreshold = 1e-10;
        ConvergenceChecker<PointValuePair> checker = null;

        // When
        new PowellOptimizer(relativeThreshold, absoluteThreshold, checker);

        // Then
        // NumberIsTooSmallException is expected to be thrown
    }
    /*  */

    /*  */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        // Given
        double relativeThreshold = 1e-10;
        double absoluteThreshold = 0.0;
        ConvergenceChecker<PointValuePair> checker = null;

        // When
        new PowellOptimizer(relativeThreshold, absoluteThreshold, checker);

        // Then
        // NotStrictlyPositiveException is expected to be thrown
    }
    /*  */
}