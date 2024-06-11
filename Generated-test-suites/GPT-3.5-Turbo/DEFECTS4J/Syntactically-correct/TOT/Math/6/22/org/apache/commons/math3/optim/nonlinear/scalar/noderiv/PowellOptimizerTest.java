import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PowellOptimizerTest {
    PowellOptimizer optimizer;
    double rel = 1.0e-10;
    double abs = 1.0e-10;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        new PowellOptimizer(0, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallException() {
        new PowellOptimizer(abs, abs);
    }

    @Test
    public void testDoOptimize() {
        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {1, 2, 3};
        ConvergenceChecker<PointValuePair> checker = null;

        double[] result = optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return FastMath.pow(point[0]-2, 2) + FastMath.pow(point[1]-3, 2) + FastMath.pow(point[2]-4, 2);
            }
        }, goal, startPoint, checker).getPointRef();

        double[] expectedResult = {2, 3, 4};

        assertTrue(MathArrays.distance(result, expectedResult) < abs);
    }

}