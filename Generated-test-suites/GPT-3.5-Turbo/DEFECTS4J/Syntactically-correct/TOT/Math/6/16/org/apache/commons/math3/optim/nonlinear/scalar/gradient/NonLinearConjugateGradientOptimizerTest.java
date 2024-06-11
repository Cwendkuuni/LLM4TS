import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    
    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,new SimpleValueChecker());
    }

    @Test
    public void testDoOptimize() {
        // Typical use case
        double[] startPoint = {1, 2};
        double[] expectedPoint = {0, 0}; // expected optimum point
        double expectedObjectiveValue = 0; // expected objective function value
        PointValuePair expectedOptimum = new PointValuePair(expectedPoint, expectedObjectiveValue);
        
        PointValuePair optimum = optimizer.optimize(100, new TestObjectiveFunction(), GoalType.MINIMIZE, startPoint);
        
        Assert.assertArrayEquals(expectedOptimum.getPoint(), optimum.getPoint(), 1e-10);
        Assert.assertEquals(expectedOptimum.getValue(), optimum.getValue(), 1e-10);
        
        // Edge case: maximum iterations reached
        startPoint = new double[] {1, 2, 3};
        optimum = optimizer.optimize(1, new TestObjectiveFunction(), GoalType.MINIMIZE, startPoint);
        Assert.assertNull("Maximum iterations reached, should return null", optimum);
        
        // Error scenario: no bracket can be found
        startPoint = new double[] {5, 5};
        try {
            optimizer.setInitialStep(-1); // force initial step to be negative
            optimum = optimizer.optimize(100, new TestObjectiveFunction(), GoalType.MINIMIZE, startPoint);
            Assert.fail("MathIllegalStateException should be thrown");
        } catch (MathIllegalStateException e) {
            // Exception expected
        }
        
    }
    
    private static class TestObjectiveFunction implements DifferentiableMultivariateFunction {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double value(double[] point) {
            double x = point[0];
            double y = point[1];
            return x*x + y*y; // f(x, y) = x^2 + y^2
        }

        @Override
        public double[] gradient(double[] point) {
            double x = point[0];
            double y = point[1];
            double[] gradient = new double[2];
            gradient[0] = 2*x; // df/dx = 2x
            gradient[1] = 2*y; // df/dy = 2y
            return gradient;
        }
    }
}