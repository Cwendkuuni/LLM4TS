import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.ConvergenceCheckerWithHistory;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {
    // 

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = {1, 2, 3};
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        // Check the dimension of the point
        Assert.assertEquals(startPoint.length, optimum.getPoint().length);

        // Check the value of the objective function at the optimum
        Assert.assertEquals(0.0, optimum.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new PowellOptimizer(1e-6, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new PowellOptimizer(1e-18, 1e-6);
    }

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceCheckerWithHistory<>();
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, convergenceChecker);
        Assert.assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    // 
}