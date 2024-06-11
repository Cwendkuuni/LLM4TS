import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optimization.leastsquares.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.leastsquares.GaussNewtonOptimizerTest;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.nonlinear.function.Axes;
import org.apache.commons.math3.optimization.nonlinear.function.PolynomialFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link GaussNewtonOptimizer}.
 *
 * @version $Id$
 */
public class GaussNewtonOptimizerTest extends GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer_simpleLinearRegression() {
        final double[] targets = {1.0, 2.0, 3.0, 4.0, 5.0};
        final double[] inputs = {2.0, 3.0, 4.0, 5.0, 6.0};
        final double[] initialGuess = {2.0, 1.0};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        final PointValuePair solution = optimizer.optimize(new PolynomialFunction(1, 1, targets), GoalType.MINIMIZE);

        Assert.assertArrayEquals(new double[]{3.0, 1.0}, solution.getPoint(), 0.01);
        Assert.assertEquals(12.0, solution.getValue(), 0.01);
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testGaussNewtonOptimizer_singularMatrix() {
        final double[] targets = {1.0, 0.0, 0.0};
        final double[] inputs = {2.0, 0.0, 0.0};
        final double[] initialGuess = {1.0, 1.0};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001), false);

        optimizer.optimize(new PolynomialFunction(2, 2, targets), GoalType.MINIMIZE);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_nullTargets() {
        final double[] targets = null;
        final double[] inputs = {2.0, 3.0, 4.0, 5.0, 6.0};
        final double[] initialGuess = {2.0, 1.0};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        optimizer.optimize(new PolynomialFunction(1, 1, targets), GoalType.MINIMIZE);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_nullInitialGuess() {
        final double[] targets = {1.0, 2.0, 3.0, 4.0, 5.0};
        final double[] inputs = {2.0, 3.0, 4.0, 5.0, 6.0};
        final double[] initialGuess = null;

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        optimizer.optimize(new PolynomialFunction(1, 1, targets), GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizer_nonConvergentChecker() {
        final double[] targets = {1.0, 2.0, 3.0, 4.0, 5.0};
        final double[] inputs = {2.0, 3.0, 4.0, 5.0, 6.0};
        final double[] initialGuess = {2.0, 1.0};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new Object(), 0.0);

        optimizer.optimize(new PolynomialFunction(1, 1, targets), GoalType.MINIMIZE);
    }

    @Test
    public void testGaussNewtonOptimizer_noFunctionDefined() {
        final double[] targets = {};
        final double[] inputs = {};
        final double[] initialGuess = {};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        try {
            optimizer.optimize(null, GoalType.MINIMIZE);
            Assert.fail("Exception expected, optimization started");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizer_noGradientDefined() {
        final double[] targets = {1.0, 2.0, 3.0, 4.0, 5.0};
        final double[] inputs = {2.0, 3.0, 4.0, 5.0, 6.0};
        final double[] initialGuess = {2.0, 1.0};

        final GoalType goalType = GoalType.MINIMIZE;
        final PolynomialFunction function = new PolynomialFunction(1, 0, targets);

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        try {
            optimizer.optimize(function, goalType);
            Assert.fail("Exception expected, optimization started");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizer_simpleQuadraticRegression() {
        final double[] targets = {1.0, 4.0, 9.0, 16.0, 25.0};
        final double[] inputs = {0.0, 1.0, 2.0, 3.0, 4.0};
        final double[] initialGuess = {1.0, 2.0};

        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(initialGuess, new SimplePointValueChecker(0.001, 0.001));

        final PointValuePair solution = optimizer.optimize(new Axes(5).map(i -> i * i), GoalType.MINIMIZE);

        Assert.assertArrayEquals(new double[]{2.5, 5.25}, solution.getPoint(), 0.01);
        Assert.assertEquals(112.25, solution.getValue(), 0.01);
    }
}