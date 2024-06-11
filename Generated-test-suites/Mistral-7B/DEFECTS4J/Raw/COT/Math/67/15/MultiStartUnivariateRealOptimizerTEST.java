import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethodOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testConstructor() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        Assert.assertNotNull(optimizer.getOptima());
    }

    @Test
    public void testOptimizeFunctionality() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.getValue().isNaN());
        Assert.assertEquals(MyFunction.MIN_VALUE, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithSameStartingPoint() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 1, new Random());
        try {
            optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Number of starts should be greater than 1", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNegativeNumberOfStarts() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        try {
            MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, -5, new Random());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Number of starts should be greater than or equal to 0", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNoIterationsLeft() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(1));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        optimizer.setMaxIterations(0);
        try {
            optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            Assert.assertEquals("Maximal number of iterations reached", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithNoFunctionEvaluationsLeft() {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(0));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        optimizer.setMaxEvaluations(0);
        try {
            optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            Assert.assertEquals("Maximal number of evaluations reached", e.getMessage());
        }
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
        double[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(new double[]{MyFunction.MIN_X, MyFunction.MIN_X, MyFunction.MIN_X, MyFunction.MIN_X, MyFunction.MIN_X, MyFunction.MIN_X}, optima, 0.001);
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealFunction function = new SimpleBoundingMethod(new MyFunction(), new SimpleBoundingMethodOptimizer(100));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(function, 5, new Random());
        optimizer.optimize(GoalType.MINIMIZE, -5.0, 5.0);
        double[] optimaValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{MyFunction.MIN_VALUE, MyFunction.MIN_VALUE, MyFunction.MIN_VALUE, MyFunction.MIN_VALUE, MyFunction.MIN_VALUE, MyFunction.MIN_VALUE}, optimaValues, 0.001);
    }

    private static class MyFunction implements UnivariateRealFunction {
        static final double MIN_VALUE = 1.0;
        static final double MIN_X = -5.0;

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(Math.PI * x), 2) + Math.pow(Math.cos(Math.PI * x), 2);
        }
    }

}