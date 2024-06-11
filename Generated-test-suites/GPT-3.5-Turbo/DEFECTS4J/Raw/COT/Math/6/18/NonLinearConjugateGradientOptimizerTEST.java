import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        optimizer.setInitialStep(0.01);

        // Test Case 1
        double[] startPoint1 = {0, 0};
        PointValuePair expected1 = new PointValuePair(new double[]{0, 0}, 0.0);
        PointValuePair actual1 = optimizer.optimize(startPoint1);
        Assert.assertEquals(expected1, actual1);

        // Test Case 2
        double[] startPoint2 = {1, 2};
        PointValuePair expected2 = new PointValuePair(new double[]{1, 1}, 0.0);
        PointValuePair actual2 = optimizer.optimize(startPoint2);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        optimizer.setInitialStep(0.01);

        // Test Case 1
        double initialStep1 = -0.01;
        optimizer.setInitialStep(initialStep1);
        double expected1 = 1.0;
        double actual1 = optimizer.getInitialStep();
        Assert.assertEquals(expected1, actual1, 0.0);

        // Test Case 2
        double initialStep2 = 0.05;
        optimizer.setInitialStep(initialStep2);
        double expected2 = 0.05;
        double actual2 = optimizer.getInitialStep();
        Assert.assertEquals(expected2, actual2, 0.0);

        // Test Case 3
        double initialStep3 = 0.0;
        optimizer.setInitialStep(initialStep3);
        double expected3 = 1.0;
        double actual3 = optimizer.getInitialStep();
        Assert.assertEquals(expected3, actual3, 0.0);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        double a = 0.0;
        double h = 0.01;

        // Test Case 1
        UnivariateFunction f1 = new LineSearchFunction(new double[]{1, 1});
        double expected1 = 0.01;
        double actual1 = optimizer.findUpperBound(f1, a, h);
        Assert.assertEquals(expected1, actual1, 0.0);

        // Test Case 2
        UnivariateFunction f2 = new LineSearchFunction(new double[]{-1, -1});
        double expected2 = 0.01;
        double actual2 = optimizer.findUpperBound(f2, a, h);
        Assert.assertEquals(expected2, actual2, 0.0);

        // Test Case 3
        UnivariateFunction f3 = new LineSearchFunction(new double[]{-1, 1});
        double expected3 = 0.01;
        double actual3 = optimizer.findUpperBound(f3, a, h);
        Assert.assertEquals(expected3, actual3, 0.0);
    }
}