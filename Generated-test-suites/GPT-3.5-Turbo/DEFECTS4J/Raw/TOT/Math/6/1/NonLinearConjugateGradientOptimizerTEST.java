
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker.PointValuePairChecker;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                 new PointValuePairChecker(1e-6, 1e-6));
        optimizer.setMaxEvaluations(100);

        // Test with a 1-dimensional function
        UnivariateFunction univariateFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        PointValuePair result = optimizer.optimize(100, univariateFunction, GoalType.MINIMIZE, -5, 5);
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-6);

        // Test with a 2-dimensional function
        MultivariateFunction multivariateFunction = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };
        result = optimizer.optimize(100, multivariateFunction, GoalType.MINIMIZE, new double[]{-5, -5}, new double[]{5, 5});
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testMaxEvaluationsExceeded() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                 new PointValuePairChecker(1e-6, 1e-6));
        optimizer.setMaxEvaluations(100);
        UnivariateFunction univariateFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(99, univariateFunction, GoalType.MINIMIZE, -5, 5);
    }

}
