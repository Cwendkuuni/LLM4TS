import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    // Test case for constructor with ConvergenceChecker parameter
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
    }

    // Test case for constructor with valid parameters
    @Test
    public void testConstructor() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    // Test case for constructor with relative threshold less than minimum
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidRel() {
        double rel = 1e-20;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    // Test case for constructor with absolute threshold <= 0
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidAbs() {
        double rel = 1e-5;
        double abs = -1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    // Test case for doOptimize() method
    // Testing an optimization for a unimodal convex function
    @Test
    public void testDoOptimizeConvex() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the convex function: f(x) = x^2
        UnivariateFunction convexFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // Find the minimum of the convex function
        double minValue = optimizer.optimize(convexFunction, GoalType.MINIMIZE, 0, 1).getValue();

        Assert.assertEquals(0.0, minValue, 0.0);
    }

    // Test case for doOptimize() method
    // Testing an optimization for a unimodal concave function
    @Test
    public void testDoOptimizeConcave() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the concave function: f(x) = -x^2
        UnivariateFunction concaveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };

        // Find the maximum of the concave function
        double maxValue = optimizer.optimize(concaveFunction, GoalType.MAXIMIZE, -1, 0).getValue();

        Assert.assertEquals(0.0, maxValue, 0.0);
    }

    // Test case for doOptimize() method
    // Testing an optimization for a function with multiple local minima
    @Test
    public void testDoOptimizeMultiMinima() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the function with multiple local minima: f(x) = (x + 2)(x - 1)(x - 3)(x - 4)
        UnivariateFunction multiMinimaFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x + 2) * (x - 1) * (x - 3) * (x - 4);
            }
        };

        // Find the minimum of the function
        double minValue = optimizer.optimize(multiMinimaFunction, GoalType.MINIMIZE, -5, 5).getValue();

        Assert.assertEquals(1.0, minValue, 0.0);
    }

    // Test case for doOptimize() method
    // Testing an optimization for a function with no minimum or maximum
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeNoMinMax() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the function with no minimum or maximum: f(x) = x
        UnivariateFunction noMinMaxFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        // Find the minimum or maximum of the function
        optimizer.optimize(noMinMaxFunction, GoalType.MINIMIZE, -1, 1);
    }

    // Test case for doOptimize() method
    // Testing an optimization for a function with error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeError() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the function with error scenario: f(x) = 1 / x
        UnivariateFunction errorFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }
        };

        // Find the minimum of the function
        optimizer.optimize(errorFunction, GoalType.MINIMIZE, 0, 1);
    }

}