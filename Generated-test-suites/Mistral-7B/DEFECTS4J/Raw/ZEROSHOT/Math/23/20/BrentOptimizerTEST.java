import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double getInitialX() {
                return 1.0;
            }
        };

        optimizer.initialize(initialGuess);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals("The optimization did not converge.", true, optimizer.isConverged());
        Assert.assertEquals("The optimization did not find the correct minimum.", -1.0099977688682245, result.getX(), 1e-5);
        Assert.assertEquals("The optimization did not find the correct minimum value.", -3.1347872452393355, result.getValue(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-8, 1e-5, null);
    }
}